package com.example.myweather.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myweather.Activity.Login_Activity;
import com.example.myweather.Application.MyApplication;
import com.example.myweather.R;
import com.example.myweather.retrofit.RetrofitClient;
import com.example.myweather.retrofit.UserRetrofitInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainMenuMypageFragment extends Fragment {
    ViewGroup viewGroup;
    Button mypage_btn_adrress;
    TextView mypage_tv_nickname, mypage_tv_email, mypage_tv_update_email_check, mypage_tv_update_pw_check, signupTvDo, signupTvSigunGu, signupTvEupMyeonDong;
    UserRetrofitInterface userRetrofitInterface;
    private SharedPreferences preferences;

    public MainMenuMypageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = getContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
        userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_mypage, container, false);
        mypage_tv_nickname = viewGroup.findViewById(R.id.mypage_tv_nickname);
        mypage_tv_email = viewGroup.findViewById(R.id.mypage_tv_email);

        mypage_tv_update_email_check = viewGroup.findViewById(R.id.mypage_ed_update_email_check);
        mypage_tv_update_pw_check = viewGroup.findViewById(R.id.mypage_ed_update_pw_check);

        EditText mypage_ed_update_email = viewGroup.findViewById(R.id.mypage_ed_update_email);
        Button mypage_btn_update_email = viewGroup.findViewById(R.id.mypage_btn_update_email);
        EditText mypage_ed_update_pw = viewGroup.findViewById(R.id.mypage_ed_update_pw);
        Button mypage_btn_update_pw = viewGroup.findViewById(R.id.mypage_btn_update_pw);

        Button mypage_logout_btn = viewGroup.findViewById(R.id.mypage_logout_btn);

        signupTvDo = viewGroup.findViewById(R.id.sign_up_tv_do);
        signupTvSigunGu = viewGroup.findViewById(R.id.sign_up_tv_si_gun_gu);
        signupTvEupMyeonDong = viewGroup.findViewById(R.id.sign_up_tv_eup_myeon_dong);

        //RequestActivity 에서 전달한 번들 저장
        Bundle bundle = getArguments();
        JSONObject json = null;
        //Log.i("JSON","mypage : "+bundle.getString("data"));

        Log.i("JSON", "mypage : " + bundle);
        try {
            json = new JSONObject(bundle.getString("data"));

            mypage_tv_nickname.setText("닉네임 : " + json.get("nickname"));
            mypage_tv_email.setText(json.get("email").toString());
            Log.i("JSON", "hogyun"+json.toString());
            Log.i("JSON", "hogyun");
        } catch (JSONException e) {
            Log.i("JSON", "변환 에러 : " + e.toString());
            e.printStackTrace();
        }

        //이메일, 패스워드 영문,숫자만 & 최대길이 조절
//        mypage_ed_update_email.setFilters(new InputFilter[]{EmailFilter,new InputFilter.LengthFilter(25)});
        mypage_ed_update_pw.setFilters(new InputFilter[]{PwFilter,new InputFilter.LengthFilter(16)});

        // pw 수정
        mypage_btn_update_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map object = new HashMap();

                    if (mypage_ed_update_pw.length() > 7 && mypage_ed_update_pw.length() < 17) {
                        object.put("pw", mypage_ed_update_pw.getText().toString());
                        mypage_tv_update_pw_check.setText(" ");
                    }else if(mypage_ed_update_pw.length()<8 || mypage_ed_update_pw.length()>16) {
                        mypage_tv_update_pw_check.setVisibility(View.VISIBLE);
                        mypage_tv_update_pw_check.setText("PW는 8~16자의 알파벳, 숫자만 사용 가능합니다.");
                        mypage_tv_update_pw_check.setTextColor(Color.RED);
                        customToastView("다시 입력해주세요.");
                }

                Call call = userRetrofitInterface.updateData("update/userPw", preferences.getString("accessToken", ""), object);
                call.enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(Call<Map> call, Response<Map> response) {
                        if (response.isSuccessful()) {
                            customToastView("비밀번호가 변경 되었습니다. 다시 로그인 해주세요.");
                            startActivity(new Intent(MyApplication.getAppContext(), Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("JSON", "onFailure");

                        customToastView("비밀번호 변경 실패, 다시 시도해 주세요.");
                    }
                });
                mypage_ed_update_pw.setText("");

            }
        });

        // 이메일 수정
        mypage_btn_update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                JSONObject json = null;

                Map object = new HashMap();
                try {
                    json = new JSONObject(bundle.getString("data"));
                    if(mypage_ed_update_email.getText().toString().contains("@")==true &&
                            mypage_ed_update_email.getText().toString().contains(".")==true) {
                        if(mypage_ed_update_email.getText().toString().equals(json.get("email").toString())){
                            mypage_tv_update_email_check.setVisibility(View.VISIBLE);
                            mypage_tv_update_email_check.setText("중복된 이메일 입니다.");
                            mypage_tv_update_email_check.setTextColor(Color.RED);
                        }
                        else{
                            object.put("email", mypage_ed_update_email.getText().toString());
                            mypage_tv_email.setText(mypage_ed_update_email.getText().toString());
                            mypage_tv_update_email_check.setText("");
                        }
                    }else if(mypage_ed_update_email.getText().toString().contains("@")==false ||
                            mypage_ed_update_email.getText().toString().contains(".")==false){
                        mypage_tv_update_email_check.setVisibility(View.VISIBLE);
                        mypage_tv_update_email_check.setText("잘못된 형식입니다.");
                        mypage_tv_update_email_check.setTextColor(Color.RED);
                        customToastView("다시 입력해주세요.");
                    }

                }catch (Exception e){

                }

                Call call = userRetrofitInterface.updateData("update/userEmail", preferences.getString("accessToken", ""), object);
                call.enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(Call<Map> call, Response<Map> response) {
                        if (response.isSuccessful()) {
                            mypage_tv_update_email_check.setText(" ");
                            customToastView("이메일 변경 성공");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("JSON", "onFailure");
                        startActivity(new Intent(MyApplication.getAppContext(), Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        customToastView("이메일 변경 실패");
                    }
                });
                mypage_ed_update_email.setText("");
            }
        });
        //로그아웃 버튼
        mypage_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                Call call = userRetrofitInterface.logOut(preferences.getString("refreshToken", ""));
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        customToastView("로그아웃");
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                    }
                });
                editor.clear();
                editor.commit();
                startActivity(new Intent(getContext(), Login_Activity.class));

            }
        });

        // Inflate the layout for this fragment
        return viewGroup;


    }

    public void customToastView(String text) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) viewGroup.findViewById(R.id.toast_layout));
        TextView textview = layout.findViewById(R.id.toast);
        textview.setText(text);
        Toast toastView = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
//        toastView.setGravity(Gravity.CENTER, 0, 800);
        toastView.setView(layout);
        toastView.show();
    }
    public InputFilter PwFilter = new InputFilter(){
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dset, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]");
            if(!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };

    public InputFilter EmailFilter = new InputFilter(){
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dset, int dstart, int dend) {
            Pattern ps = Pattern.compile("\"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$\"");
            if(!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };
}
