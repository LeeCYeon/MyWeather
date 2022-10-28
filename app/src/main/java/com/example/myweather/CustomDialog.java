package com.example.myweather;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myweather.R;
import com.example.myweather.retrofit.RetrofitClient;
import com.example.myweather.retrofit.UserRetrofitInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDialog extends Dialog {
    private Context mcontext;

    String[] si_do = {"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시",
            "울산광역시", "경기도", "강원도", "충청남도", "충청북도", "경상남도", "경상북도", "전라남도", "전라북도", "제주특별자치도"};
    List<String> si_gun_gu = new ArrayList<>();
    List<String> eup_myeon_dong = new ArrayList<>();
    String do1 = null;
    List allres = new ArrayList();
    List allres2 = new ArrayList();

    UserRetrofitInterface userRetrofitInterface;
    Map<String, String> userLocation = new HashMap<>();

    Button btnInput;
    TextView tv_do, tv_si_gun_gu, tv_eup_myeon_dong;

    CustomDialogListener iTextView;

    // for spinner
    LinearLayout spinnerLayout;
    android.widget.Spinner sp1, sp2, sp3;
    ArrayAdapter<String> adapter1, adapter2, adapter3;

    private CustomDialogListener customDialogListener;

    public CustomDialog(Context mcontext) {
        super(mcontext);
        this.mcontext = mcontext;
    }

    public interface CustomDialogListener{
        void setViews(String _do, String siGunGu, String eupMyeonDong);
    }

    // 호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener) {
        this.customDialogListener = customDialogListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        tv_do = findViewById(R.id.custom_dialog_tv_do);
        tv_si_gun_gu = findViewById(R.id.custom_dialog_tv_si_gun_gu);
        tv_eup_myeon_dong = findViewById(R.id.custom_dialog_tv_eup_myeon_dong);

        userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

        //String 오름차순 정렬
        Arrays.sort(si_do);
        //spinner
        spinnerLayout = findViewById(R.id.layout_spinner);
        sp1 = findViewById(R.id.spinner1);
        sp2 = findViewById(R.id.spinner2);
        sp3 = findViewById(R.id.spinner3);

        adapter1 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, si_do);
        adapter2 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, si_gun_gu);
        adapter3 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, eup_myeon_dong);

        sp1.setAdapter(adapter1);
        sp2.setAdapter(adapter2);
        sp3.setAdapter(adapter3);

        sp1.setOnItemSelectedListener(listener);
        sp2.setOnItemSelectedListener(listener);
        sp3.setOnItemSelectedListener(listener);

        tv_do.setText("*시/도");
        tv_si_gun_gu.setText("*시/군/구");
        tv_eup_myeon_dong.setText("*읍/면/동");

        btnInput = findViewById(R.id.custom_dialog_btn_input);
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogListener.setViews(userLocation.get("_do"), userLocation.get("siGunGu"), userLocation.get("eupMyeonDong"));
                dismiss();
            }
        });
    }

    Map<String, String> getUserLocation(){
        return this.userLocation;
    }

    //SPINNER
    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, @NonNull View view, int position, long l) {
            Log.i("ming", "*********** ITEM SELECTED !!! ***************");
            Log.i("ming", Integer.toString(view.getId()));
            Log.i("ming", Integer.toString(adapterView.getId()));

            switch (adapterView.getId()) {
                case R.id.spinner1:
                    Log.i("ming", "sp1");
                    try {
                        spinner1(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.spinner2:
                    Log.i("ming", "sp2");
                    try {
                        spinner2(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.spinner3:
                    Log.i("ming", "sp3");
                    try {
                        spinner3(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void spinner1(int position) throws Exception {
        Log.i("ming", "spinner1 method");
        do1 = si_do[position];

        userLocation.put("_do", do1);
        Log.e("hogyun", "도: " + userLocation.get("_do") + " : " + userLocation);
        Call<Map<String, List>> all = userRetrofitInterface._do(userLocation);
        all.enqueue(new Callback<Map<String, List>>() {
            @Override
            public void onResponse(Call<Map<String, List>> call, Response<Map<String, List>> response) {
                allres = response.body().get("siGunGuMap");
                si_gun_gu.clear();
                Log.i("hogyun", "서버 연결 성공 1번 스피너" + allres);
                for (int i = 0; i < allres.size(); i++) {
                    si_gun_gu.add(allres.get(i).toString());
//                    //오름차순 정렬
//                    Collections.sort(si_gun_gu);
                }
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                sp2.setAdapter(adapter2);
            }

            @Override
            public void onFailure(Call<Map<String, List>> call, Throwable t) {
                Log.i("hogyun", "연결실패 1번 스피너" + t.getMessage());
            }
        });
        return;
    }

    public void spinner2(int position) throws Exception {
        Log.i("ming", "spinner2 method");
        userLocation.put("siGunGu", si_gun_gu.get(position).toString());
        Log.e("hogyun", "시군구: " + userLocation.get("siGunGu") + " : " + userLocation);
        Call<Map<String, List>> all = userRetrofitInterface.siGunGu(userLocation);
        all.enqueue(new Callback<Map<String, List>>() {
            @Override
            public void onResponse(Call<Map<String, List>> call, Response<Map<String, List>> response) {
                allres2 = response.body().get("eupMyeonDongMap");
                eup_myeon_dong.clear();
                Log.i("hogyun", "서버 연결 성공 2번 스피너" + allres2);
                for (int i = 0; i < allres2.size(); i++) {
                    eup_myeon_dong.add(allres2.get(i).toString());
                    //오름 차순 정렬
                    Collections.sort(eup_myeon_dong);
                }
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                sp3.setAdapter(adapter3);
            }

            @Override
            public void onFailure(Call<Map<String, List>> call, Throwable t) {
                Log.i("hogyun", "연결실패 2번스피너" + t.getMessage());
            }
        });
        return;
    }

    public void spinner3(int position) throws Exception {
        Log.i("ming", "spinner2 method");
        userLocation.put("eupMyeonDong", eup_myeon_dong.get(position).toString());
        Log.e("hogyun", "읍면동: " + userLocation.get("eupMyeonDong") + " : " + userLocation);

        return;
    }
}