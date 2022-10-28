package com.example.myweather.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;
import com.example.myweather.RecyclerViewDecoration;
import com.example.myweather.WeekHeaderAdapter;
import com.example.myweather.WeekHeaderItem;
import com.example.myweather.WeekHorizonAdapter;
import com.example.myweather.WeekHorizonData;
import com.example.myweather.WeekItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Response;


public class MainMenuWeekFragment extends Fragment {

    ViewGroup viewGroup;

    //10일 예보
    private RecyclerView mHorizonView;
    private WeekHorizonAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    // 3일예보
    private RecyclerView recyclerView;
    private List<WeekHeaderItem> headerItemList;
    private WeekHeaderAdapter headerAdapter;
    private LinearLayoutManager hLayoutManager;

    //단기 리스트
    ArrayList<String> tmpList = new ArrayList<>();
    ArrayList<String> tmxList = new ArrayList<>();
    ArrayList<String> tmnList = new ArrayList<>();
    ArrayList<String> skyList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();

    //중기 리스트
    ArrayList<String> long_min = new ArrayList<>();
    ArrayList<String> long_max = new ArrayList<>();
    ArrayList<String> long_sky_am = new ArrayList<>();
    ArrayList<String> long_sky_pm = new ArrayList<>();
    ArrayList<String> long_rain_am = new ArrayList<>();
    ArrayList<String> long_rain_pm = new ArrayList<>();

    //파싱
    JSONArray jsonArray_short; // "shortFcst"
    JSONArray jsonArray_long;  // "longFcst"

    //날짜, 날짜 인덱스
    String day2, day3, day4, day5, day6, day7, day8, day9, day10, day11;
    int dateIndex_2, dateIndex_3, dateIndex_4;
    ImageView btn_info, btn_cancel = null;
    LinearLayout week_info_layout = null;

    public MainMenuWeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_week, container, false);

        RecyclerViewDecoration decoration_width = new RecyclerViewDecoration(20);

        mHorizonView = (RecyclerView) viewGroup.findViewById(R.id.vertical_recyclerview);
        mHorizonView.addItemDecoration(decoration_width);

        //10일 예보
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHorizonView.setLayoutManager(mLayoutManager);

        mAdapter = new WeekHorizonAdapter();
        mHorizonView.setAdapter(mAdapter);

        //JSON 파싱
        Bundle bundle = getArguments();
        JSONObject json, json2, json3 = null;
        try {
            json = new JSONObject(bundle.getString("data"));
            json2 = new JSONObject(bundle.getString("data"));
            json3 = new JSONObject(bundle.getString("data"));

            // 단기예보(1-2일)
            JSONObject shortData = (JSONObject) json.get("shortFcst");

            //단기예보 파싱(1,2,3일)
            jsonArray_short = (JSONArray) shortData.get("shortData");

            for (int i = 0; i < jsonArray_short.length(); i++) {
                json2 = jsonArray_short.getJSONObject(i);
                String max = json2.getString("tmx");
                String min = json2.getString("tmn");
                String sky = json2.getString("sky");
                String date = json2.getString("fcstDate");
                String time = json2.getString("fcstTime");
                String tmp = json2.getString("tmp");

                tmxList.add(max);
                tmnList.add(min);
                skyList.add(sky);
                tmpList.add(tmp);

                //여기서 날짜별로 파싱
                dateList.add(date);
                timeList.add(time);
            }

            // 중기예보(기온) 파싱
            // 3일 후 최저/최고 기온 ~ 10일 후 최저/최고 기온
            jsonArray_long = (JSONArray) json.get("longFcst");
            for (int i = 0; i < jsonArray_long.length(); i++) {
                json3 = jsonArray_long.getJSONObject(i);
                String max = json3.getString("taMax");
                String min = json3.getString("taMin");

                long_max.add(max);
                long_min.add(min);

            }
            Log.i("week","\nmax: "+tmxList+"\nmin: "+tmnList+"\ndateList: "+dateList);
            Log.i("week","단기예보 파싱: "+ json2.toString());

            //중기 육상 예보 파싱
            // 하늘 상태 (3일 후~10일 후)
            JSONObject landData = (JSONObject) json.get("landFcst");

            //am 파싱
            String sky_3_am = landData.getString("wf3Am");
            String sky_4_am = landData.getString("wf4Am");
            String sky_5_am = landData.getString("wf5Am");
            String sky_6_am = landData.getString("wf6Am");
            String sky_7_am = landData.getString("wf7Am");

            String sky_3_pm = landData.getString("wf3Pm");
            String sky_4_pm = landData.getString("wf4Pm");
            String sky_5_pm = landData.getString("wf5Pm");
            String sky_6_pm = landData.getString("wf6Pm");
            String sky_7_pm = landData.getString("wf7Pm");


            //pm 파싱
            String sky_8 = landData.getString("wf8");
            String sky_9 = landData.getString("wf9");
            String sky_10 = landData.getString("wf10");

            // am 강수량 파싱
            String rain_3_am = landData.getString("rnSt3Am");
            String rain_4_am = landData.getString("rnSt4Am");
            String rain_5_am = landData.getString("rnSt5Am");
            String rain_6_am = landData.getString("rnSt6Am");
            String rain_7_am = landData.getString("rnSt7Am");

            //pm 강수량 파싱
            String rain_3_pm = landData.getString("rnSt3Pm");
            String rain_4_pm = landData.getString("rnSt4Pm");
            String rain_5_pm = landData.getString("rnSt5Pm");
            String rain_6_pm = landData.getString("rnSt6Pm");
            String rain_7_pm = landData.getString("rnSt7Pm");

            //8, 9, 10 강수량 파싱 (pm 리스트에 저장 index:5, 6, 7)
            String rain_8 = landData.getString("rnSt8");
            String rain_9 = landData.getString("rnSt9");
            String rain_10 = landData.getString("rnSt10");

            long_sky_am.add(sky_3_am);
            long_sky_am.add(sky_4_am);
            long_sky_am.add(sky_5_am);
            long_sky_am.add(sky_6_am);
            long_sky_am.add(sky_7_am);

            long_sky_pm.add(sky_3_pm);
            long_sky_pm.add(sky_4_pm);
            long_sky_pm.add(sky_5_pm);
            long_sky_pm.add(sky_6_pm);
            long_sky_pm.add(sky_7_pm);

            long_sky_pm.add(sky_8);
            long_sky_pm.add(sky_9);
            long_sky_pm.add(sky_10);

            long_rain_am.add(rain_3_am);
            long_rain_am.add(rain_4_am);
            long_rain_am.add(rain_5_am);
            long_rain_am.add(rain_6_am);
            long_rain_am.add(rain_7_am);

            long_rain_pm.add(rain_3_pm);
            long_rain_pm.add(rain_4_pm);
            long_rain_pm.add(rain_5_pm);
            long_rain_pm.add(rain_6_pm);
            long_rain_pm.add(rain_7_pm);
            long_rain_pm.add(rain_8);
            long_rain_pm.add(rain_9);
            long_rain_pm.add(rain_10);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // e:No adapter attached; skipping layout
        // > 레이아웃 매니저는 set 했는데 Orientation지정을 하지 않아서 오류 > 수정 완료
        recyclerView = viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        hLayoutManager = new LinearLayoutManager(getContext());
        hLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(hLayoutManager);

        //1,2,3일 후 날짜
        // String > Calender > 더하기 연산 > String으로 반환
        String date = dateList.get(0);
        try {
            day2 = AddDate(date, 0, 0, 1);
            day3 = AddDate(date, 0, 0, 2);
            day4 = AddDate(date, 0, 0, 3);
            day5 = AddDate(date, 0, 0, 4);
            day6 = AddDate(date, 0, 0, 5);
            day7 = AddDate(date, 0, 0, 6);
            day8 = AddDate(date, 0, 0, 7);
            day9 = AddDate(date, 0, 0, 8);
            day10 = AddDate(date, 0, 0, 9);
            day11 = AddDate(date, 0, 0, 10);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // 날짜 비교 > 다음날 index 구하기
        for (int i = 0; i < dateList.size(); i++) {
            if (dateList.get(i).equals(day2)) {
                dateIndex_2 = i;
                break;
                //반복 수행을 멈추고 반복문 외부로 이동
            }
        }

        for (int i = dateIndex_2; i < dateList.size(); i++) {
            if (dateList.get(i).equals(day3)) {
                dateIndex_3 = i;
                break;
            }
        }

        for (int i = dateIndex_3; i < dateList.size(); i++) {
            if (dateList.get(i).equals(day4)) { //이렇게 하면 첫 시작 지점이 아니라 계속 가게되는데 처음 발견하면 멈추도록 수정
                dateIndex_4 = i;
                break;
            }
        }


        Log.i("test", dateList.get(0));
        Log.i("test", "오늘:" + date + "\n내일:" + day2 + "\n모레:" + day3);
        Log.i("test", "day2 index: " + dateIndex_2 + "\n day3 index" + dateIndex_3 + "\n day4 index" + dateIndex_4 + "\nday5 index: 필요없음");


        //List<Item> 생성
        List<WeekItem> items1 = new ArrayList<>();
        for (int i = 0; i < dateIndex_2; i++) {

            if (i % 2 == 0) {
                items1.add(new WeekItem(tmpList.get(i), timeList.get(i).substring(0, 2)));
            }

        }
        List<WeekItem> items2 = new ArrayList<>();
        for (int i = dateIndex_2; i < dateIndex_3; i++) {


            //값 전부 들어옴
            if (i % 2 == 0) {
                Log.d("JSON", "314" + i + "    " + timeList.get(i).toString() +
                        "  " + timeList.toString());
                items2.add(new WeekItem(tmpList.get(i), timeList.get(i).substring(0, 2)));
            }
        }

        List<WeekItem> items3 = new ArrayList<>();
        for (int i = dateIndex_3; i < dateIndex_4; i++) {

            //값 전부 들어옴
            if (i % 2 == 0) {
                items3.add(new WeekItem(tmpList.get(i), timeList.get(i).substring(0, 2)));
            }
        }

        List<WeekItem> items4 = new ArrayList<>();
        for (int i = dateIndex_4; i < dateList.size(); i++) {

            //값 전부 들어옴
            if (i % 2 == 0) {
                items4.add(new WeekItem(tmpList.get(i), timeList.get(i).substring(0, 2)));
            }
            Log.i("test", "4" + items4.toString());
        }

        headerItemList = new ArrayList<>();
        headerItemList.add(new WeekHeaderItem("화", dateList.get(0), tmnList.get(0), tmxList.get(0), items1, skyList.get(0)));
        headerItemList.add(new WeekHeaderItem("수", dateList.get(dateIndex_2), tmnList.get(dateIndex_2), tmxList.get(dateIndex_2), items2, skyList.get(dateIndex_2)));
        headerItemList.add(new WeekHeaderItem("목", dateList.get(dateIndex_3), tmnList.get(dateIndex_3), tmxList.get(dateIndex_3), items3, skyList.get(dateIndex_3)));

        headerAdapter = new WeekHeaderAdapter(headerItemList);
        recyclerView.setAdapter(headerAdapter);

//        //수정중
        ArrayList<WeekHorizonData> data = new ArrayList<>();


        data.add(new WeekHorizonData(day4, long_sky_am.get(0), long_sky_pm.get(0), long_rain_am.get(0), long_rain_pm.get(0), long_max.get(0), long_min.get(0)));
        data.add(new WeekHorizonData(day5, long_sky_am.get(1), long_sky_pm.get(1), long_rain_am.get(1), long_rain_pm.get(1), long_max.get(1), long_min.get(1)));
        data.add(new WeekHorizonData(day6, long_sky_am.get(2), long_sky_pm.get(2), long_rain_am.get(2), long_rain_pm.get(2), long_max.get(2), long_min.get(2)));
        data.add(new WeekHorizonData(day7, long_sky_am.get(3), long_sky_pm.get(3), long_rain_am.get(3), long_rain_pm.get(3), long_max.get(3), long_min.get(3)));
        data.add(new WeekHorizonData(day8, long_sky_am.get(4), long_sky_pm.get(4), long_rain_am.get(4), long_rain_pm.get(4), long_max.get(4), long_min.get(4)));
        //오전, 오후 글씨 어떻게 처리할 지
        data.add(new WeekHorizonData(day9, long_sky_pm.get(5), "", long_rain_pm.get(5), "", long_max.get(5), long_min.get(5)));
        data.add(new WeekHorizonData(day10, long_sky_pm.get(6), "", long_rain_pm.get(6), "", long_max.get(6), long_min.get(6)));
        data.add(new WeekHorizonData(day11, long_sky_pm.get(7), "", long_rain_pm.get(7), "", long_max.get(7), long_min.get(7)));

        // set Data
        mAdapter.setData(data);

//       //점심 먹고 위치 위로 올리기
        mAdapter.notifyDataSetChanged();


        btn_cancel =viewGroup.findViewById(R.id.week_btn_cancel);

        btn_info = viewGroup.findViewById(R.id.iv_week_btn_info);
        week_info_layout = viewGroup.findViewById(R.id.week_info);
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(week_info_layout.getVisibility()==View.INVISIBLE){
                    week_info_layout.setVisibility(View.VISIBLE);
                }else{
                    week_info_layout.setVisibility(View.INVISIBLE);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                week_info_layout.setVisibility(View.INVISIBLE);
            }
        });
        return viewGroup;
    }


    // 날짜 더하기 연산
    private String AddDate(String strDate, int year, int month, int day) throws Exception {

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dtFormat2 = new SimpleDateFormat("MM월 dd일");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        Date dt = dtFormat.parse(strDate);
        cal.setTime(dt);
        cal2.setTime(dt);
        //day22 학인됨
//            day22= dtFormat2.format(cal2.getTime());
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);

        dtFormat.format(cal.getTime());
        return dtFormat.format(cal.getTime());
    }


}