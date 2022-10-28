package com.example.myweather.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myweather.Fragment.FindMenuIDFragment;
import com.example.myweather.Fragment.FindMenuPWFragment;
import com.example.myweather.R;
import com.example.myweather.retrofit.UserRetrofitInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindActivity extends AppCompatActivity {

    private TabLayout tabs;
    private FragmentManager fragmentManager= getSupportFragmentManager();
    private FindMenuIDFragment find_id_fragment;
    private FindMenuPWFragment find_pw_fragment;
    int pos;
    String URL = null;
    Call accessToken_Call;
    UserRetrofitInterface userRetrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        find_id_fragment= new FindMenuIDFragment();
        find_pw_fragment = new FindMenuPWFragment();

        Intent intent = new Intent(this.getIntent());

       fragmentManager.beginTransaction().replace(R.id.find_fragment_container,find_id_fragment).commit();
        tabs = findViewById(R.id.tab_find);
        tabs.addTab(tabs.newTab().setText("ID 찾기"));
        tabs.addTab(tabs.newTab().setText("PW 찾기"));

        URL = "requestData/long-weather"; //Server Controller 에 연결할 URL
//        accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
//        accessToken_Call.enqueue(new Callback<Map>() {
//            @Override
//            public void onResponse(Call<Map> call, Response<Map> response) {
//                if(response.isSuccessful()){
//                    Bundle bundle = new Bundle();
//                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                    String noEscapeStr = gson.toJson(response.body().get("data")).toString();
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(noEscapeStr);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    bundle.putString("data", jsonObject.toString());
//                    weekFragment.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.fragment_container, weekFragment).commit();
//                }
//            }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos= tab.getPosition();
                Log.i("test","선택된 탭:"+pos);

                Fragment selected= null;
                if(pos==0){
                    selected= find_id_fragment;

                }else if(pos==1) {
                    selected= find_pw_fragment;
                }
                fragmentManager.beginTransaction().replace(R.id.find_fragment_container,selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}