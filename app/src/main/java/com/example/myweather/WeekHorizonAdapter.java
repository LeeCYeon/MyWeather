package com.example.myweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeekHorizonAdapter extends RecyclerView.Adapter<WeekViewHolder>{

    private ArrayList<WeekHorizonData> horizonDatas;

    public void setData(ArrayList<WeekHorizonData> list){
        horizonDatas = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.week_horizon_recycler_items, parent, false);
        WeekViewHolder holder = new WeekViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {

        //data- 날짜 + 요일
        WeekHorizonData data = horizonDatas.get(position);
        String day=data.getDate_txt();
        holder.date.setText(day.substring(4,6)+"월  "+ day.substring(6,8)+"일");
        holder.tv_rain_am.setText(""+data.getRain_am_txt()+"%");
        holder.tv_rain_pm.setText(data.getRain_pm_txt()+"%");
        holder.tv_temp_max.setText(data.getMax_txt()+"℃");
        holder.tv_temp_min.setText(data.getMin_txt()+"℃   / ");

        //getsky_ am/pm따로 가져올건지 결정
        //todo 이미지 맞게 수정하기
        switch (data.getSky_am_txt()){
            case "맑음":
                holder.iv_icon_am.setImageResource(R.drawable.c_sky_sun);
                break;
            case "구름많음":
                holder.iv_icon_am.setImageResource(R.drawable.c_sky_cloudy);
                break;
            case  "흐림":
                holder.iv_icon_am.setImageResource(R.drawable.c_pty_dark);
                break;

        }
        switch (data.getSky_pm_txt()){
            case "맑음":
                holder.iv_icon_pm.setImageResource(R.drawable.c_sky_sun);
                break;
            case "구름많음":
                holder.iv_icon_pm.setImageResource(R.drawable.c_sky_cloudy);
                break;
            case "흐림":
                holder.iv_icon_pm.setImageResource(R.drawable.c_pty_dark);
                break;
            case "":
                holder.tv_pm.setText("       하루");
                holder.tv_am.setText("");
                holder.tv_rain_pm.setVisibility(View.GONE);

                //11일 후 하늘상태 오후에 ""값을 줘도 계속 아이콘이 뜸 11일만
                //일단 이미지를 제거함 > todo 원인 찾기
                holder.iv_icon_pm.setVisibility(View.GONE);

                break;
        }
    }

    @Override
    public int getItemCount() {
        return horizonDatas.size();
    }

    public void addItem(WeekHorizonData horizonData){ horizonDatas.add(horizonData);}

}
