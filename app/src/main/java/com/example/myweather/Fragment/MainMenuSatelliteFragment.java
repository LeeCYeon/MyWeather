package com.example.myweather.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.myweather.R;

import java.util.ArrayList;
import java.util.List;


public class MainMenuSatelliteFragment extends Fragment {
    ViewGroup viewGroup;
//    ArrayList<String> radarData;

    ImageView ivImg;
    int index= 0;
    Thread thread= null;
    List<String> imgUrlList= new ArrayList<>();;
    ImageView btn_play, btn_pause;
    RequestOptions options;
    ProgressBar progressBar;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public MainMenuSatelliteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_satellite,container,false);
        // Inflate the layout for this fragment


        ivImg=(ImageView)viewGroup.findViewById(R.id.iv_rader);


        btn_play=(ImageView)viewGroup.findViewById(R.id.btn_play);
        btn_pause=(ImageView)viewGroup.findViewById(R.id.btn_pause);
        progressBar= (ProgressBar)viewGroup.findViewById(R.id.progressBar);

        options= new RequestOptions().override(250,250).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().centerCrop().dontAnimate();


        //서버에서 데이터 받아옴
        Bundle bundle = getArguments();
        Log.e("JSON","위성 화면 : "+bundle.getString("data").length());
        String str1 = bundle.getString("data").replace("[", "").replace("]", "").replace(" ", "");
        String str[] = str1.split(",");
        for(int i =0;i<str.length;i++) {
            imgUrlList.add(str[i]);
        }

        Glide.with(getContext()).load(imgUrlList.get(0)).apply(options).into(ivImg);
        progressBar.setMax(imgUrlList.size());

        //재생 버튼 이벤트
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_play.setVisibility(View.INVISIBLE);
                btn_pause.setVisibility(View.VISIBLE);

                if (thread == null)
                    thread = new Thread(new ImageRunnable());
                thread.start();

            }
        });

        //중지 버튼 이벤트
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_pause.setVisibility(View.INVISIBLE);
                btn_play.setVisibility(View.VISIBLE);

                //스레드가 일시 정지 상태에 있을 때
                //InterruptedException 예외를 발생시켜 정상종료 하도록
                thread.interrupt();
                thread = null;

            }
        });

        return viewGroup;
    }

    class ImageRunnable implements Runnable {

        Handler handler = new Handler();

        @Override
        public void run() {
            while (true) {

                //Runnable 객체를 MessageQue에 전달
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(!imgUrlList.get(index).equals(ivImg.getTag())){
                            ivImg.setTag(null);
                            //DiskCacheStrategy.ALL : 모든 이미지에 대하여 캐싱
                            if(index % 10 == 0){
                                Glide.with(getContext()).load(imgUrlList.get(index))
                                        .apply(options).into(ivImg);

                                progressBar.setProgress(index);
                            }
                            ivImg.setTag(imgUrlList.get(index));
                        }
                        }

                });

                index++;
                if (index == imgUrlList.size()){
                    index = 0;
                    progressBar.setProgress(0);
                }
                try {

                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}