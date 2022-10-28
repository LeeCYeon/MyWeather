package com.example.myweather;

// 리스트 item안에 들어갈 내용
// week_horizon_recycler_items.xml에 들어갈 데이터

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeekHorizonData {

    private String date_txt,sky_am_txt,sky_pm_txt, rain_am_txt,rain_pm_txt , max_txt, min_txt ;

    public WeekHorizonData(String date_txt, String sky_am_txt, String sky_pm_txt, String rain_am_txt, String rain_pm_txt, String max_txt, String min_txt) {
        this.date_txt = date_txt;
        this.sky_am_txt = sky_am_txt;
        this.sky_pm_txt = sky_pm_txt;
        this.rain_am_txt = rain_am_txt;
        this.rain_pm_txt = rain_pm_txt;
        this.max_txt = max_txt;
        this.min_txt = min_txt;
    }

    public String getMax_txt() {
        return max_txt;
    }

    public String getMin_txt() {
        return min_txt;
    }

    public String getDate_txt() {
        return date_txt;
    }


    public String getSky_am_txt() {
        return sky_am_txt;
    }

    public String getSky_pm_txt() {
        return sky_pm_txt;
    }

    public String getRain_am_txt() {
        return rain_am_txt;
    }

    public String getRain_pm_txt() {
        return rain_pm_txt;
    }
}
