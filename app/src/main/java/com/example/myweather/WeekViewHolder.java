package com.example.myweather;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;

//현재 보여지고 있는 아이템만을 바인딩 시켜 사용하게끔
public class WeekViewHolder  extends RecyclerView.ViewHolder{

     public ImageView iv_icon_am, iv_icon_pm;
     public TextView date, tv_rain_am, tv_rain_pm , tv_am, tv_pm, tv_temp_min, tv_temp_max ;

    public WeekViewHolder(@NonNull View itemView) {
        super(itemView);

        date= (TextView)itemView.findViewById(R.id.week_long_date);

        tv_am=(TextView)itemView.findViewById(R.id.week_long_pm);
        tv_pm= (TextView)itemView.findViewById(R.id.week_long_am);

        iv_icon_am = (ImageView) itemView.findViewById(R.id.week_long_icon_am);
        iv_icon_pm = (ImageView) itemView.findViewById(R.id.week_long_icon_pm);
        tv_rain_am = (TextView) itemView.findViewById(R.id.week_long_rain_am);
        tv_rain_pm = (TextView) itemView.findViewById(R.id.week_long_rain_pm);

        tv_temp_min = (TextView) itemView.findViewById(R.id.week_long_min);
        tv_temp_max = (TextView) itemView.findViewById(R.id.week_long_max);


    }


}
