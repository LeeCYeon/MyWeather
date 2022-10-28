package com.example.myweather;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

        ArrayList<TimeItem> data;

        public TimeAdapter(ArrayList<TimeItem> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, null);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TimeItem item = data.get(position);
            holder.temp.setText(item.getTemp() + "°");
            switch (item.getPty()) {
                case "없음": {
                    switch (item.getSky()){
                        case "맑음":{
                            holder.icon.setImageResource(R.drawable.c_sky_sun);
                            break;
                        }
                        case "구름많음":{
                            holder.icon.setImageResource(R.drawable.c_sky_cloudy);
                            break;
                        }
                        case "흐림":{
                            holder.icon.setImageResource(R.drawable.c_pty_dark);
                            break;
                        }

                    }
                    break;

                }
                case "비":{
                    holder.icon.setImageResource(R.drawable.c_pty_rain);
                    break;
                }
                case "비/눈":{
                    holder.icon.setImageResource(R.drawable.c_pty_rain_snow);
                    break;
                }
                case "눈":{
                    holder.icon.setImageResource(R.drawable.c_pty_snow);
                    break;
                }
                case "소나기":{
                    holder.icon.setImageResource(R.drawable.c_pty_rain);
                    break;
                }

                default: {
                    break;
                }
            }
            holder.hour.setText(item.getHour().substring(0,2) + "시");
        }



        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView temp, hour;
            ImageView icon;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                temp = itemView.findViewById(R.id.tv_temp);
                icon = itemView.findViewById(R.id.iv_icon);
                hour = itemView.findViewById(R.id.tv_hour);
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class TimeItem {
            private String temp, sky, pty, hour;

            public String getTemp() {
                return temp;
            }

            String getSky() {
                return sky;
            }

            String getPty() {return pty;}

            String getHour() {
                return hour;
            }

            public TimeItem(String temp,String sky,String pty, String hour) {
                this.temp = temp;
                this.sky = sky;
                this.pty = pty;
                this.hour = hour;
            }
        }
    }