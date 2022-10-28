package com.example.myweather.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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


public class SplashActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    UserRetrofitInterface userRetrofitInterface;
    ImageView iv_splash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();
        preferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        iv_splash= (ImageView) findViewById(R.id.iv_logo);
        Glide.with(this).load(R.raw.logo2).into(iv_splash);

        //autoLogin -> 스플래시 화면으로 이동할 예정
        if(preferences.getBoolean("autoLogin",false)){ //뒤에 false = defValue 값
            Map object = new HashMap();
            object.put("refreshToken",preferences.getString("refreshToken",""));

            Call<Map> call = userRetrofitInterface.autoLogin(object);
            call.enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if(response.isSuccessful()){
                        if(response.body().get("result").toString().equals("true")){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            String test = new Gson().toJson(response.body().get("object"));
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            try {
                                Response_UserVo response_userVo = objectMapper.readValue(test, Response_UserVo.class);
                                intent.putExtra("uservo", response_userVo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("JSON","자동 로그인 성공");
                            startActivity(intent);
                        }else if(response.body().get("result").toString().equals("false")){
                            Log.e("JSON","refresh Token 만료, 재로그인 할 것!");
                            editor.clear();
                            editor.commit(); // 동기적 처리 //editor.apply(); //비동기적 처리
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                }
            });
        }else {
            Intent intent = new Intent(this, Login_Activity.class);
            startActivity(intent);
            finish();
        }


    }


}