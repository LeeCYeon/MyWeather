package com.example.myweather.retrofit;

import com.example.myweather.vo.Request_UserVo;
import com.example.myweather.vo.Response_UserVo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

//UserRetrofitInterface는 어떤 파라미터와 함께 어떤 Method로 요청을 보내고, 어떤 형태로 응답을 받을 것인지
//정의한다.
public interface UserRetrofitInterface {


    //POST는 서버에 데이터를 보낼 때 사용
    @POST("info/login")
    Call<Response_UserVo> login(@Body Map jsonUser);
    @POST("info/autoLogin")
    Call<Map> autoLogin(@Body Map jsonUser);

    @POST("info/logout")
    Call<Map> logOut(@Header("refreshToken") String token);

    @POST("info/sign-up")
    Call<String> signUp(@Body Request_UserVo jsonUser);

    @POST("info/sign-up/idCheck")
    Call<String> idCheck(@Body Map id);

    @POST("info/sign-up/nicknameCheck")
    Call<String> nickName(@Body Map nickName);

    //GET는 서버의 데이터를 가져올 때 사용
    @GET
    Call<Map> access_Call(@Url String url, @Header("ACCESS-TOKEN") String token);

    @POST
    Call<Map> updateData(@Url String url,@Header("ACCESS-TOKEN") String token,@Body Map data);

    @POST("/api/location/si-gun-gu")
    Call<Map<String, List>> _do(@Body Map _do);

    @POST("/api/location/eup-myeon-dong")
    Call<Map<String, List>> siGunGu(@Body Map siGunGu);

}
