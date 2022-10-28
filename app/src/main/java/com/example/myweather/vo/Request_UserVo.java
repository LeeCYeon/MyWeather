package com.example.myweather.vo;


public class Request_UserVo {
    private String id;
    private String nickname;
    private String pw;
    private String email;
    private String _do;
    private String siGunGu;
    private String eupMyeonDong;

    public Request_UserVo(String id, String nickname, String pw, String email, String _do, String siGunGu, String eupMyeonDong) {
        this.id = id;
        this.nickname = nickname;
        this.pw = pw;
        this.email = email;
        this._do = _do;
        this.siGunGu = siGunGu;
        this.eupMyeonDong = eupMyeonDong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String get_do() {
        return _do;
    }

    public void set_do(String _do) {
        this._do = _do;
    }

    public String getSiGunGu() {
        return siGunGu;
    }

    public void setSiGunGu(String siGunGu) {
        this.siGunGu = siGunGu;
    }

    public String getEupMyeonDong() {
        return eupMyeonDong;
    }

    public void setEupMyeonDong(String eupMyeonDong) {
        this.eupMyeonDong = eupMyeonDong;
    }
}
