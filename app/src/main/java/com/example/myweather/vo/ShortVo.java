package com.example.myweather.vo;

public class ShortVo {
    private String fcstDate;
    private String fcstTime;
    private String SKY;
    private String PTY;
    private String TMP;
    private String TMN;
    private String TMX;

    public ShortVo(String fcstDate, String fcstTime,
                   String SKY, String PTY, String TMP,
                   String TMN, String TMX) {
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.SKY = SKY;
        this.PTY = PTY;
        this.TMP = TMP;
        this.TMN = TMN;
        this.TMX = TMX;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    public String getSKY() {
        return SKY;
    }

    public void setSKY(String SKY) {
        this.SKY = SKY;
    }

    public String getPTY() {
        return PTY;
    }

    public void setPTY(String PTY) {
        this.PTY = PTY;
    }

    public String getTMP() {
        return TMP;
    }

    public void setTMP(String TMP) {
        this.TMP = TMP;
    }

    public String getTMN() {
        return TMN;
    }

    public void setTMN(String TMN) {
        this.TMN = TMN;
    }

    public String getTMX() {
        return TMX;
    }

    public void setTMX(String TMX) {
        this.TMX = TMX;
    }

    @Override
    public String toString() {
        return "ShortVo{" +
                "fcstDate='" + fcstDate + '\'' +
                ", fcstTime='" + fcstTime + '\'' +
                ", SKY='" + SKY + '\'' +
                ", PTY='" + PTY + '\'' +
                ", TMP='" + TMP + '\'' +
                ", TMN='" + TMN + '\'' +
                ", TMX='" + TMX + '\'' +
                '}';
    }
}
