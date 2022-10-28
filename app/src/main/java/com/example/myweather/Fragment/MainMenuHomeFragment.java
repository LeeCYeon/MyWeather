package com.example.myweather.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myweather.Utils.getNoPoint;
import static com.example.myweather.Utils.setChart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myweather.Application.MyApplication;
import com.example.myweather.R;
import com.example.myweather.TimeAdapter;
import com.example.myweather.databinding.FragmentMainMenuHomeBinding;
import com.example.myweather.retrofit.RetrofitClient;
import com.example.myweather.retrofit.UserRetrofitInterface;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuHomeFragment extends Fragment {
    // 모든 view binding
    static FragmentMainMenuHomeBinding fbinding;

    String[] amMsg;           //강수확률 핸들러 msg
    String[] pmMsg;           //강수확률 핸들러 msg
    String differMsg;
    String differMsg2;
    Bundle bundle = new Bundle();


    String residence; // 거주지
    JSONObject nowDataJson = new JSONObject(); // 초단기실황
    JSONObject sunJson = new JSONObject(); // 일출, 일몰
    JSONObject shortDataJson = new JSONObject(); // 단기예보

    private SharedPreferences preferences; // Token

    public MainMenuHomeFragment() {}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserRetrofitInterface userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();
        preferences = MyApplication.getAppContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        fbinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu_home, container, false);
        View root = fbinding.getRoot();

        Glide.with(getContext()).load(R.drawable.sunrise).into(fbinding.homeImgSunrise);
        setDate();

        String CallUrl = "requestData/now-fcst"; // Server에 요청할 URL
        Call accessToken_Call = userRetrofitInterface.access_Call(CallUrl, preferences.getString("accessToken", ""));
        accessToken_Call.enqueue(callback);

        return root;
    }

    /*******************************************************************/
    // CallBack Method
    Callback<Map>  callback = new Callback<Map> () {
        String noEscapeStr;

        @Override
        public void onResponse(Call<Map>  call, Response<Map>  response) {
            Log.i("ming", "CALLBACK");
            JSONObject json = null;
            JSONArray nowDataArr = null; // 초단기실황 Array
            JSONArray sunArr = null; // 일출, 일몰 Array
            try{
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                noEscapeStr = gson.toJson(response.body().get("data")).toString();

                json = new JSONObject(noEscapeStr);
                Log.e("ming", "json : " + json.toString());
                Log.e("ming", "json sunList: " + json.get("sunList"));
                residence = json.get("residence").toString(); // 거주지
                nowDataArr = json.getJSONArray("nowData");
                nowDataJson = (JSONObject) nowDataArr.get(0); // 초단기실황
                sunArr = json.getJSONArray("sunList");
                sunJson = (JSONObject) sunArr.get(0); // 일출, 일몰
                shortDataJson = (JSONObject) json.get("nowFcstData"); // 단기예보

                parseShort(); // 단기예보 파싱

                // 초단기실황으로 현재 기온 표시
                Log.e("ming","NOW DATA JSON" + nowDataJson.toString());
                String nowTemp = nowDataJson.getString("t1H");
                Log.e("ming", "t1H"+nowTemp);
                fbinding.homeTvNowTemp.setText(getNoPoint(nowTemp) + "℃");

                // 일출 시간
                StringBuffer sunrise = new StringBuffer();
                sunrise.append(String.valueOf(sunJson.get("sunrise")));
                sunrise.insert(2, ":");
                // 일몰 시간
                StringBuffer sunset = new StringBuffer();
                sunset.append(String.valueOf(sunJson.get("sunset")));
                sunset.insert(2, ":");
                // 일출, 일몰 표시
                fbinding.homeTvSunrise.setText("일출\n" + sunrise);
                fbinding.homeTvSunset.setText("일몰\n" + sunset);

                //최저기온 최고기온 표시
                for (int i = 0; i < 1; i++) {
                    fbinding.homeTvMinTemp.setText(getNoPoint(tmnList.get(i)) + "°");
                    fbinding.homeTvMaxTemp.setText(getNoPoint(tmxList.get(i)) + "°");
                }

                // 지역명 표시
                String residences[] = residence.split("\\s");
                for (int i = 0; i < residences.length; i++) {
                    Log.e("ming", "RESIDENCE = " + residences[i]);
                    fbinding.homeTvMainCity.setText(residences[i]);
                }

                // GIF 표시
                setGIF();

                //차트, 리사이클러뷰 띄우기
                setTimeTab(timeList, tmpList, skyList, ptyList);

                // 강수확률 크롤링
                new Thread() {
                    @Override
                    public void run() {
                        Document doc = null;
                        try {
                            String URL = "https://search.naver.com/search.naver?" +
                                    "where=nexearch&sm=top_hty&fbm=0&ie=utf8" +
                                    "&query=" + residence.replace("\\s","+") + "날씨";

                            Log.e("ming", "URL : " + URL);
                            Log.e("ming", "residence : " + residence.replace("\\s","+"));
                            doc = Jsoup.connect(URL).get();    //URL 웹
                            // 사이트에 있는 html 코드를 다 끌어오기

                            Elements elements = doc.select(".weather_left");
                            Elements elements_difference = doc.select(".summary");


                            amMsg = elements.text().split(" ");
                            pmMsg = elements.text().split(" ");
                            differMsg = elements_difference.text();



                            bundle.putString("am_rain", amMsg[1]);
                            bundle.putString("pm_rain", pmMsg[3]);
                            bundle.putString("difference",differMsg);


                            Message msg = handler.obtainMessage();
                            Message msg2 = handler.obtainMessage();
                            Message msg3 = handler.obtainMessage();

                            msg.setData(bundle);
                            msg2.setData(bundle);
                            msg3.setData(bundle);

                            handler.sendMessage(msg);
                            handler.sendMessage(msg2);
                            handler.sendMessage(msg3);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<Map>  call, Throwable t) {

        }
    };

    /*******************************************************************/
    //오늘 날짜 설정
    private void setDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("M월 d일 EEEE");
        String getDay = sdf.format(date);
        fbinding.homeTvTime.setText(getDay);
    }

    /*******************************************************************/
    ArrayList<String> tmpList = new ArrayList<>();
    ArrayList<String> tmxList = new ArrayList<>();
    ArrayList<String> tmnList = new ArrayList<>();
    ArrayList<String> skyList = new ArrayList<>();
    ArrayList<String> ptyList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();

    //단기예보 파싱(1,2,3일)
    public void parseShort() throws JSONException {
        JSONArray shortArr = (JSONArray) shortDataJson.get("shortData");
        for (int i = 0; i < shortArr.length(); i++) {
            JSONObject jsonObj = shortArr.getJSONObject(i);

            tmxList.add(jsonObj.getString("tmx"));
            tmnList.add(jsonObj.getString("tmn"));
            skyList.add(jsonObj.getString("sky"));
            tmpList.add(jsonObj.getString("tmp"));
            ptyList.add(jsonObj.getString("pty"));

            // 날짜별로 파싱
            dateList.add(jsonObj.getString("fcstDate"));
            timeList.add(jsonObj.getString("fcstTime"));
        }
    }
    /*******************************************************************/
    // 현재 시각
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("kk");
    String getTime = sdf.format(date);

    /*******************************************************************/
    // Chart & RecyclerView
    private void setTimeTab(ArrayList<String> baseTime, ArrayList<String> temp, ArrayList<String> sky, ArrayList<String> pty) {
        ArrayList<TimeAdapter.TimeItem> data = new ArrayList<>();

        for (int i = 0; i < baseTime.size(); i++) {
            if (Integer.parseInt(getTime) == Integer.parseInt(baseTime.get(i))/100 )
//                    < 60 || Integer.parseInt(getTime) - Integer.parseInt(baseTime.get(i))/100 > 1000)
            {
                Log.e("ming","getTime : "+ getTime);
                for (int j = i; j < i + 8; j++) {
                    data.add(new TimeAdapter.TimeItem(getNoPoint(temp.get(j)), sky.get(j), pty.get(j), baseTime.get(j)));
                }
                break;
            }
        }
        TimeAdapter timeAdapter = new TimeAdapter(data);
        fbinding.homeRecycler.setAdapter(timeAdapter);
        fbinding.homeRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<Entry> value = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            value.add(new Entry(i, Float.parseFloat(data.get(i).getTemp())));
        }
        setChart(value, fbinding.homeChart, getActivity());
    }

    /*******************************************************************/
    // 현재 하늘상태에 따라 GIF 설정
    public void setGIF() throws JSONException {

        for (int i = 0; i < 12; i++) {
            if (Integer.parseInt(getTime) == Integer.parseInt(timeList.get(i)) / 100) {
                String nowPty = nowDataJson.getString("pty");
                switch (nowPty) {
                    case "없음":
                        switch (skyList.get(i)) {
                            case "맑음":
                                Glide.with(getContext()).load(R.drawable.sky_sun).into(fbinding.homeImgHomeIcon);
                                break;
                            case "구름많음":
                                Glide.with(getContext()).load(R.drawable.sky_cloud).into(fbinding.homeImgHomeIcon);
                                break;
                            case "흐림":
                                Glide.with(getContext()).load(R.drawable.sky_dark).into(fbinding.homeImgHomeIcon);
                                break;
                        } // end of switch skyList
                        break;
                    case "비":
                        Glide.with(getContext()).load(R.drawable.pty_rain).into(fbinding.homeImgHomeIcon);
                        break;
                    case "빗방울":
                        Glide.with(getContext()).load(R.drawable.pty_raindrop).into(fbinding.homeImgHomeIcon);
                        break;
                    case "비/눈":
                        Glide.with(getContext()).load(R.drawable.pty_rain_snow).into(fbinding.homeImgHomeIcon);
                        break;
                    case "빗방울눈날림":
                        Glide.with(getContext()).load(R.drawable.pty_rain_snow).into(fbinding.homeImgHomeIcon);
                        break;
                    case "눈날림":
                        Glide.with(getContext()).load(R.drawable.pty_snow_wind).into(fbinding.homeImgHomeIcon);
                        break;
                    case "눈":
                        Glide.with(getContext()).load(R.drawable.pty_snow).into(fbinding.homeImgHomeIcon);
                        break;
                } // end of switch nowPty
            } // end of if
        } // end of for
    }// end of GIF

    /*******************************************************************/
    //강수확률 핸들러
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            bundle = msg.getData();
            String differ[] = bundle.getString("difference").split("\\s");

            fbinding.homeTvAmRain.setText(bundle.getString("am_rain"));
            fbinding.homeTvPmRain.setText(bundle.getString("pm_rain"));
            fbinding.homeTvYesterday.setText(differ[0]);
            fbinding.homeTvCompYesterday.setText(differ[1]);
            fbinding.homeTvHighLow.setText(differ[2]);
        }
    };
}