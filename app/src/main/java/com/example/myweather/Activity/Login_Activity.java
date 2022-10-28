package com.example.myweather.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.R;
import com.example.myweather.retrofit.RetrofitClient;
import com.example.myweather.retrofit.UserRetrofitInterface;
import com.example.myweather.vo.Response_UserVo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {
    UserRetrofitInterface userRetrofitInterface;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserRetrofitInterface userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

        EditText login_ed_id = findViewById(R.id.login_ed_id);
        EditText login_ed_pw = findViewById(R.id.login_ed_pw);
        TextView login_tv_findPW = findViewById(R.id.login_tv_findPW);
        TextView login_tv_signUp = findViewById(R.id.login_tv_signUp);
        Button login_btn = findViewById(R.id.login_btn);
        CheckBox login_autologin = findViewById(R.id.login_autologin);
        preferences = getSharedPreferences("UserInfo",MODE_PRIVATE);

        //로그인
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map object = new HashMap();
                object.put("id",login_ed_id.getText().toString());
                object.put("pw",login_ed_pw.getText().toString());

                Call<Response_UserVo> call = userRetrofitInterface.login(object);
                call.enqueue(new Callback<Response_UserVo>() {
                    @Override
                    public void onResponse(Call<Response_UserVo> call, Response<Response_UserVo> response) { //통신에 성공했을 경우
                        Log.e("JSON", "통신 성공");
                        Log.e("JSON", response.body().toString());
                        if (response.isSuccessful()) {//응답이 왔을 경우
                            if (response.body().getSuccess() == true) { //로그인 성공했을 경우
                                customToastView("로그인 성공");
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("accessToken",response.body().getAccessToken());
                                editor.putString("refreshToken",response.body().getRefreshToken());
                                editor.putBoolean("autoLogin",false);
                                if(login_autologin.isChecked()){ // 자동로그인 체크
                                    editor.putBoolean("autoLogin",true);
                                }
                                editor.commit();
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                String test = new Gson().toJson(response.body());
                                ObjectMapper objectMapper = new ObjectMapper();
                                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                try {
                                    Response_UserVo response_userVo = objectMapper.readValue(test, Response_UserVo.class);
                                    Log.e("JSON", "object 변환 : " + response_userVo);
                                    intent.putExtra("uservo", response_userVo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            } else if ((response.body().getSuccess() == false)) {
                                Log.e("JSON", "로그인 실패");
                                if (response.body().getErrorId() == 404) {
                                    customToastView("로그인 실패");
                                    Log.e("JSON", "아이디가 없습니다.");
                                } else if (response.body().getErrorPw() == 404) {
                                    customToastView("비밀번호가 다릅니다.");
                                    Log.e("JSON", "비밀번호 다릅니다.");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response_UserVo> call, Throwable t) { //통신에 실패했을 경우
                    }
                });


            }
        });

        //Id/Pw 찾기
        login_tv_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, FindActivity.class);
                startActivity(intent);
            }
        });
        //회원가입
        login_tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Sign_Up_Activity.class);
                startActivity(intent);
            }
        });


    }
    public void customToastView(String text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout) );
        TextView textview = layout.findViewById(R.id.toast);
        textview.setText(text);
        Toast toastView = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
        toastView.setGravity(Gravity.CENTER,0,800);
        toastView.setView(layout);
        toastView.show();
    }
}