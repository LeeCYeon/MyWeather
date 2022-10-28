package com.example.myweather.retrofit;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myweather.Application.MyApplication;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomInterceptor implements Interceptor {
    private SharedPreferences preferences;
    JSONObject jsonObject=null;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Request.Builder requestBuilder = request.newBuilder();

        preferences = MyApplication.getAppContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Log.i("JSON","코드 : "+response.code()+"");

        if(response.code()==403){ // 오류일경우, A 토큰이 만료된경우
            // R 토큰 전송
            RetrofitClient.getURL();
            Response Res_RefreshToken = chain.proceed(requestBuilder.addHeader("refreshToken",preferences.getString("refreshToken","")).url(RetrofitClient.getURL()+"refresh").build());
            Log.i("JSON","refreshToken  :  "+preferences.getString("refreshToken",""));
            if(Res_RefreshToken.code()==200){ // refreshToken이 유효한 경우
                String responseData = new Gson().toJson(Res_RefreshToken.body().byteString().utf8().replace("\"",""));
                try {
                    jsonObject = new JSONObject(responseData.replace("\"",""));
                    if(jsonObject.get("result").toString().equals("false")){
                        Log.i("JSON",responseData);
                        return Res_RefreshToken;
                    }else if(jsonObject.get("result").toString().equals("true")){
                        Log.i("JSON","재발급 받은 토큰 : "+jsonObject.get("token"));
                        editor.putString("accessToken",jsonObject.get("token").toString()).commit();
                        Response Re_response = chain.proceed(request.newBuilder().removeHeader("ACCESS-TOKEN").addHeader("ACCESS-TOKEN",jsonObject.get("token").toString()).build());
                        return Re_response;
                    }
                } catch (Exception e) {
                    Log.i("JSON", "Try 에러 ");
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
}
