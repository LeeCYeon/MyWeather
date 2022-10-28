package com.example.myweather.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweather.Activity.MainActivity;
import com.example.myweather.Activity.NewsActivity2;
import com.example.myweather.NewsItem;
import com.example.myweather.NewsListAdapter;
import com.example.myweather.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainMenuNewsFragment extends Fragment {
    ViewGroup viewGroup;

    // Fragment to Fragment 를 위해 선언
    MainActivity mainactivity;

    ArrayList<String> titles= new ArrayList<>();
    ArrayList<String> pressList= new ArrayList<>();
    ArrayList<String> contentsList= new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> imgUrlList= new ArrayList<>();

    ArrayList<String> tempTitles= new ArrayList<>();
    ArrayList<String> tempPressList= new ArrayList<>();
    ArrayList<String> tempContentsList= new ArrayList<>();
    ArrayList<String> tempUrlList = new ArrayList<>();
    ArrayList<String> tempImgUrlList= new ArrayList<>();

    NewsItem listViewItem;
    Bundle bundle= new Bundle();
    NewsListAdapter adapter;
    String str;
    String encodeStr,my_location;
    TextView date;
    ListView listView;



    Date currentTime = Calendar.getInstance().getTime();

    public MainMenuNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //자신이 attach 되어 있는 Activity를 호출: getActivity()
        mainactivity= (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainactivity= null;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_news,container,false);
        // Inflate the layout for this fragment

//         유저의 거주지
        Bundle bundle = getArguments();
//        Log.e("JSON",bundle.getString("data",""));


        listView = viewGroup.findViewById(R.id.listView1);

        adapter = new NewsListAdapter();

        listView.setAdapter(null);

//        my_location= "천안";
//        try {
//            encodeStr= URLEncoder.encode(my_location, "UTF-8");
//            Log.i("test",encodeStr);
//            String decodeStr = URLDecoder.decode(encodeStr, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        new Thread() {
            @Override
            public void run() {
                Document doc=null;
                try {
                    doc= Jsoup.connect("https://search.naver.com/search.naver?where=news&query="
                            +"%EB%82%A0%EC%94%A8&sm=tab_opt&sort=0&photo=0&field=0&pd=4&ds=&de=&docid=&related=0&mynews=0&office_type=0&office_section_code=0&news_office_checked=&nso=so%3Ar%2Cp%3A1d&is_sug_officeid=0").get();

                    for(int i=0; i<5; i++){
                        //뉴스 타이틀
                        Element title= doc.select(".news_tit").get(i);
                        String titleTxt= title.text();
                        titles.add(titleTxt);

//                        //뉴스 내용(요약)
                        Elements cont =doc.select(".news_tit").get(i).nextElementSiblings();
                        String contTxt= cont.text();
                        contentsList.add(contTxt);

                        // 뉴스 url
                        Element cont2 = doc.select(".news_tit").get(i);
                        str = cont2.getElementsByAttribute("href").attr("href");
                        urlList.add(str);

                        //뉴스 이미지
                        Element cont3 = doc.select(".dsc_thumb").get(i);
                        String imgURL= cont3.getElementsByAttribute("src").attr("src");
                        imgUrlList.add(imgURL);
                        Log.i("test",imgURL);

                        //뉴스 언론사
                        //언론사
                        Element press = doc.select(".info_group").get(i).child(0);
                        String pressTxt = press.text().replace("언론사 선정","");
                        pressList.add(pressTxt);

                        //listviewItem을 전역변수에서 리스트로 생성해서 값이 계속 새로 생성되니까 같은 내용만 들어감
                        listViewItem=new NewsItem();
                        listViewItem.setPressTxt(pressList.get(i));
                        listViewItem.setTitleTxt(titles.get(i));
                        listViewItem.setConTxt(contentsList.get(i));
                        listViewItem.setUrl(urlList.get(i));
                        Log.i("JSON_t","url:"+urlList.get(i));
                        listViewItem.setImg_url(imgUrlList.get(i));

                        // 리스트뷰에 아이템 추가
                        adapter.addItem(listViewItem);

                        Message msg= handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }
//                    listView.setAdapter(adapter);

//                    //main thread가 아닌 곳에서 ui를 건들게 되면 오류가 나므로 handler에서 접근
//                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getUrl 메서드 만들기
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putString("url",urlList.get(i));//번들에 넘길 값 저장

                mainactivity.change_to_News2();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                getActivity().getSupportFragmentManager().setFragmentResult("url", bundle);
                MainMenuNewsFragment2 fragment2 = new MainMenuNewsFragment2();
                fragment2.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
            }
        });

        SimpleDateFormat format = new SimpleDateFormat("MM월 dd일", Locale.getDefault());
        String current = format.format(currentTime);
        date= (TextView)viewGroup.findViewById(R.id.tv_today);
        date.setText(current+" 뉴스");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        return viewGroup;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            adapter.notifyDataSetChanged();

        }
    };




}