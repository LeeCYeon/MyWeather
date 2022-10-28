package com.example.myweather;

import android.widget.ProgressBar;

import java.util.List;

//최상위 카테고리 (요일, 날짜)
public class WeekHeaderItem {

    private String date_txt, day_txt, min, max, sky;
    private int img_resource;

    public String getSky() {
        return sky;
    }

    public int getImg_resource() {
        return img_resource;
    }

    public void setImg_resource(int img_resource) {
        this.img_resource = img_resource;
    }

    //하위 아이템 객체를 배열로 받아 상위 카테고리에 연결하기 위해
    private List<WeekItem> childItemList;


    //Header의 화살표 클릭 시 child의 progressbar 설정을 HeadrAdapter에서 해줘야 하므로
    private ProgressBar childProgressBar;


    // 펼침/닫음 여부 확인용
    private boolean isExpandable;

    public WeekHeaderItem(String day, String date, String min, String max, List<WeekItem> childItemList, String sky) {
        this.date_txt = date;
        this.day_txt = day;
        this.min= min;
        this. max= max;
        this.childItemList = childItemList;
        this.sky = sky;

        // 기본값을 false로 주어 하위 항목을 숨김
        this.isExpandable = false;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getDate() {
        return date_txt;
    }

    public String getDay() {
        return day_txt;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<WeekItem> getChildItemList() {
        return childItemList;
    }

}
