package com.example.myweather.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

//싱글톤 패턴 적용
public class RetrofitClient {


    private static RetrofitClient instance = null;
    private static UserRetrofitInterface userRetrofitInterface;
    private static String baseUrl = "http://192.168.0.10:8080/";
   // private static final String baseUrl = "http://192.168.0.58:8080/";
//    private static String baseUrl = "http://192.168.219.104:8080/";

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new CustomInterceptor()).build();
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)//뒷 부분을 쉽게 처리할 수 있도록 인터페이스 제공
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) //서버로부터 데이터를 받아와서 원하는 타입으로 데이터를 바꾸기 위해 사용
                .build();
        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);

    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public static final String getURL(){
        return baseUrl;
    }


    public static UserRetrofitInterface getUserRetrofitInterface() {
        return userRetrofitInterface;
    }
}
