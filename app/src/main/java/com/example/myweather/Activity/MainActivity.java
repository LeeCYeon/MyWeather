package com.example.myweather.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweather.CustomDialog;
import com.example.myweather.Fragment.MainMenuHomeFragment;
import com.example.myweather.Fragment.MainMenuMypageFragment;
import com.example.myweather.Fragment.MainMenuNewsFragment;
import com.example.myweather.Fragment.MainMenuNewsFragment2;
import com.example.myweather.Fragment.MainMenuSatelliteFragment;
import com.example.myweather.Fragment.MainMenuWeekFragment;
import com.example.myweather.R;
import com.example.myweather.SwipeDismissListViewTouchListener;
import com.example.myweather.retrofit.RetrofitClient;
import com.example.myweather.retrofit.UserRetrofitInterface;
import com.example.myweather.vo.Response_UserVo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainMenuHomeFragment homeFragment = new MainMenuHomeFragment();
    private MainMenuWeekFragment weekFragment = new MainMenuWeekFragment();
    private MainMenuSatelliteFragment satelliteFragment = new MainMenuSatelliteFragment();
    private MainMenuNewsFragment newsFragment = new MainMenuNewsFragment();

    //??????
    private MainMenuNewsFragment2 newsFragment2 = new MainMenuNewsFragment2();
    private MainMenuMypageFragment mypageFragment = new MainMenuMypageFragment();


    DrawerLayout drawerLayout;
    View drawerView;
    ArrayList<String> my_locations;
    ListView lv;
    ArrayAdapter<String> itemAA;
    Button btn_input_address;
    TextView signupTvDo, signupTvSigunGu, signupTvEupMyeonDong;
    String URL;

    UserRetrofitInterface userRetrofitInterface;
    private SharedPreferences preferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn_input_address= findViewById(R.id.drawer_input_address);
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        intent = getIntent();
        userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();
        Response_UserVo response_userVo = (Response_UserVo) intent.getSerializableExtra("uservo");


        signupTvDo = findViewById(R.id.sign_up_tv_do);
        signupTvSigunGu = findViewById(R.id.sign_up_tv_si_gun_gu);
        signupTvEupMyeonDong = findViewById(R.id.sign_up_tv_eup_myeon_dong);

        TextView main_nickname = findViewById(R.id.main_nickname);
        main_nickname.setText(response_userVo.getNickname()+"??? ???????????????.");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e("JSON",item.toString());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String URL = null;
                Call accessToken_Call;
                switch (item.getItemId()) {
                    case R.id.home: // ?????? ?????? ???
                        fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
                        break;
                    case R.id.week: // ?????? ?????? ???
                        URL = "requestData/long-weather"; //Server Controller ??? ????????? URL
                        accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
                        accessToken_Call.enqueue(new Callback<Map>() {
                            @Override
                            public void onResponse(Call<Map> call, Response<Map> response) {
                                if(response.isSuccessful()){
                                    Bundle bundle = new Bundle();
                                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                    String noEscapeStr = gson.toJson(response.body().get("data")).toString();
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(noEscapeStr);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    bundle.putString("data", jsonObject.toString());
                                    weekFragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.fragment_container, weekFragment).commit();
                                }
                            }
                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.i("JSON", "onFailure");
                                startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                    case R.id.satellite: // ?????? ?????? ???
                        URL = "requestData/radar"; //Server Controller ??? ????????? URL
                        accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
                        accessToken_Call.enqueue(new Callback<Map>() {
                            @Override
                            public void onResponse(Call<Map> call, Response<Map> response) {
                                if(response.isSuccessful()){
                                    Bundle bundle = new Bundle();
                                    Log.e("JSON","?????? : "+response.body().get("data").toString());
                                    bundle.putString("data",response.body().get("data").toString());
                                    satelliteFragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.fragment_container, satelliteFragment).commit();
                                }
                            }
                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.i("JSON", "onFailure");
                                startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                    case R.id.news: // ?????? ?????? ???
                        URL = "requestData/news"; //Server Controller ??? ????????? URL
                        accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
                        accessToken_Call.enqueue(new Callback<Map>() {
                            @Override
                            public void onResponse(Call<Map> call, Response<Map> response) {
                                if(response.isSuccessful()){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("data",response.body().get("data").toString());
                                    newsFragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.fragment_container, newsFragment).commit();
                                }
                            }
                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.i("JSON", "onFailure");
                                startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                    case R.id.mypage: // ??? ?????? ?????? ???
                        URL = "requestData/MyPage"; //Server Controller ??? ????????? URL
                        accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
                        accessToken_Call.enqueue(new Callback<Map>() {
                            @Override
                            public void onResponse(Call<Map> call, Response<Map> response) {
                                if(response.isSuccessful()){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("data",response.body().get("data").toString());
                                    Log.e("JSON","Main mypage : "+response.body().toString());
                                    mypageFragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.fragment_container, mypageFragment).commit();
                                }
                            }
                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.i("JSON", "onFailure");
                                startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        break;
                }

                return true;
            }
        });

        main_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String URL = null;
                Call accessToken_Call;
                URL = "requestData/MyPage"; //Server Controller ??? ????????? URL
                accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
                accessToken_Call.enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(Call<Map> call, Response<Map> response) {
                        if(response.isSuccessful()){
                            Bundle bundle = new Bundle();
                            bundle.putString("data",response.body().get("data").toString());
                            Log.e("JSON","Main mypage : "+response.body().toString());
                            mypageFragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_container, mypageFragment).commit();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("JSON", "onFailure");
                        startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
            }
        });

//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        Button btn_open = findViewById(R.id.btn_open);

        //?????? ?????? ??? ?????? ??????(drawer) ??????
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
                String URL = "requestData/favorite-location"; //Server Controller ??? ????????? URL
                Call accessToken_Call = userRetrofitInterface.access_Call(URL,preferences.getString("accessToken",""));
                accessToken_Call.enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(Call<Map> call, Response<Map> response) {
                        if(response.isSuccessful()){
                            my_locations.clear();
                            try {
                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                JSONArray jsonArray = new JSONArray(gson.toJson(response.body().get("data")).toString());
                                Log.e("JSON",jsonArray.toString());
                                for(int i = 0 ;i<jsonArray.length();i++){
                                    my_locations.add(jsonArray.get(i).toString());
                                    itemAA.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                Log.e("JSON",e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("JSON", "onFailure");
                        startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return true;
            }
        });

        Button btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"?????? ??????",Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });


        //?????? ???????????? ?????? ?????? ????????? ??????
        my_locations= new ArrayList<>();


        lv=(ListView) findViewById(R.id.lv_myLoc);
        itemAA= new ArrayAdapter<String>(this, R.layout.drawer_row_layout,R.id.tv_location,my_locations);
        lv.setAdapter(itemAA);


        //????????? ?????? ??????
        btn_input_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
                customDialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void setViews(String _do, String siGunGu, String eupMyeonDong) {
                        Map map = new HashMap();
                        map.put("_do",_do);
                        map.put("siGunGu",siGunGu);
                        map.put("eupMyeonDong",eupMyeonDong);
                        String url = "requestData/add-favorite";
                        Call accessToken_Call = userRetrofitInterface.updateData(url,preferences.getString("accessToken",""),map);
                        accessToken_Call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if(response.isSuccessful()){
                                    Log.i("JSON", "?????? ??????");
                                }
                            }
                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.i("JSON", "onFailure");
                                startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });

                        String str= map.get("_do")+" " +map.get("siGunGu")+" "+map.get("eupMyeonDong") ;
                        my_locations.add(str);
                        itemAA.notifyDataSetChanged();
                    }
                });
                customDialog.show();
            }
        });



        // ???????????? ????????? ?????? ??? ????????? ????????????(????????? ?????? ??????)??? string?????? ?????????
        // ??????????????? string??? ?????? ?????? ????????? ?????? ????????? ????????????
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] array = my_locations.get(i).split(" ");
                Map map = new HashMap();
                map.put("_do",array[0]);
                map.put("siGunGu",array[1]);
                map.put("eupMyeonDong",array[2]);
                String url = "requestData/update_userLocation";
                Call accessToken_Call = userRetrofitInterface.updateData(url,preferences.getString("accessToken",""),map);
                accessToken_Call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.isSuccessful()){ // ?????? ??????
                            //FragmentTransaction Transaction = getSupportFragmentManager().beginTransaction();
                            //Transaction.detach(homeFragment).attach(homeFragment).commit();
                            //Transaction.replace(R.id.fragment_container, homeFragment);
                            try {
                                //TODO ???????????? ?????? ????????? ????????? ??????
                                Intent intent = getIntent();
                                finish(); //?????? ???????????? ?????? ??????
                                overridePendingTransition(0, 0); //????????? ??????????????? ?????????
                                startActivity(intent); //?????? ???????????? ????????? ??????
                                overridePendingTransition(0, 0); //????????? ??????????????? ?????????
                            }
                            catch (Exception e){
                                Log.e("JSON",e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("JSON", "onFailure");
                        startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
                Log.i("JSON",array[0] +" " + array[1]);
            }
        });

        //????????? ?????? ?????????
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(lv,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                Map map = new HashMap();
                                for (int position : reverseSortedPositions) {
                                    String[] array = itemAA.getItem(position).split(" ");
                                    map.put("_do",array[0]);
                                    map.put("siGunGu",array[1]);
                                    map.put("eupMyeonDong",array[2]);
                                    Log.e("JSON","?????? : " + itemAA.getItem(position));
                                    String url = "requestData/favorite-location";
                                    Call accessToken_Call = userRetrofitInterface.updateData(url,preferences.getString("accessToken",""),map);
                                    accessToken_Call.enqueue(new Callback<Map>() {
                                        @Override
                                        public void onResponse(Call<Map> call, Response<Map> response) {
                                            if(response.isSuccessful()){ // ?????? ??????
                                                if(response.body().get("data").toString().equals("SUCCESS")){
                                                    Log.e("JSON","?????? ??????");
                                                }else if(response.body().get("data").toString().equals("FAILURE")){
                                                    Log.e("JSON","?????? ??????");
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call call, Throwable t) {
                                            Log.i("JSON", "onFailure");
                                            startActivity(new Intent(MainActivity.this,Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                                        }
                                    });

                                    itemAA.remove(itemAA.getItem(position));
                                }
                                itemAA.notifyDataSetChanged();
                            }
                        });

        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());

    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
            Log.e("JSON","??????????????? ??? ??????");
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
    public void change_to_News2() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newsFragment2).addToBackStack(null).commit();
    }
}