package com.example.myweather;


//하위 아이템
public class WeekItem {

    private String tmp, hour;

    public String getTmp() {
        return tmp;
    }

    public String getHour() {
        return hour;
    }

    public WeekItem(String tmp, String hour) {
        this.tmp = tmp;
        this.hour = hour;
    }
}
