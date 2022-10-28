package com.example.myweather;

public class NewsItem {
    private String pressTxt;
    private String titleTxt;
    private String conTxt;
    private String url;
    private String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPressTxt() {
        return pressTxt;
    }

    public void setPressTxt(String pressTxt) {
        this.pressTxt = pressTxt;
    }

    public String getTitleTxt() {
        return titleTxt;
    }

    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
    }

    public String getConTxt() {
        return conTxt;
    }

    public void setConTxt(String conTxt) {
        this.conTxt = conTxt;
    }

    public String getUrl() {
        return url;
    }


}
