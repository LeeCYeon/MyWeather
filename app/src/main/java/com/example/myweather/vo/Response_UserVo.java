package com.example.myweather.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Response_UserVo implements Serializable {
    private String id;
    private String nickname;
    private String pw;
    private String email;
    private boolean success;
    private int errorId;
    private int errorPw;
    private String accessToken;
    private String refreshToken;

    public Response_UserVo() {
        super();
    }


    public Response_UserVo(String id, String nickname, String pw, String email, boolean success, int errorId, int errorPw, String accessToken, String refreshToken) {
        this.id = id;
        this.nickname = nickname;
        this.pw = pw;
        this.email = email;
        this.success = success;
        this.errorId = errorId;
        this.errorPw = errorPw;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public int getErrorPw() {
        return errorPw;
    }

    public void setErrorPw(int errorPw) {
        this.errorPw = errorPw;
    }

    @Override
    public String toString() {
        return "Response_UserVo{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pw='" + pw + '\'' +
                ", email='" + email + '\'' +
                ", success=" + success +
                ", errorId=" + errorId +
                ", errorPw=" + errorPw +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}