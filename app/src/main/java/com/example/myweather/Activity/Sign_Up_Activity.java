package com.example.myweather.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myweather.CustomDialog;
import com.example.myweather.R;
import com.example.myweather.retrofit.RetrofitClient;
import com.example.myweather.retrofit.UserRetrofitInterface;
import com.example.myweather.vo.Request_UserVo;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign_Up_Activity extends AppCompatActivity {
    UserRetrofitInterface userRetrofitInterface;
    private boolean idCheck = false, nicknameCheck = false, pwCheck = false, threadCheck = true;
    EditText signUpPw, signUpEdPwCheck;
    TextView signUpTvPwCheck;
    Button signUpAdrress;
    String userEmail;
    TextView signupTvDo, signupTvSigunGu, signupTvEupMyeonDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        UserRetrofitInterface userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

        EditText signUpId = findViewById(R.id.sign_up_ed_id);
        TextView sign_up_tv_duplicate_verification_id = findViewById(R.id.sign_up_tv_duplicate_verification_id);
        EditText signUpNickname = findViewById(R.id.sign_up_ed_nickname);
        TextView sign_up_tv_duplicate_verification_nickname = findViewById(R.id.sign_up_tv_duplicate_verification_nickname);
        signupTvDo = findViewById(R.id.sign_up_tv_do);
        signupTvSigunGu = findViewById(R.id.sign_up_tv_si_gun_gu);
        signupTvEupMyeonDong = findViewById(R.id.sign_up_tv_eup_myeon_dong);
        signUpAdrress=findViewById(R.id.sign_up_btn_input_address);
        signUpPw = findViewById(R.id.sign_up_ed_pw);
        signUpEdPwCheck = findViewById(R.id.sign_up_ed_pw_check);
        signUpTvPwCheck = findViewById(R.id.sign_up_tv_pw_check);

        EditText signUpEmail = findViewById(R.id.sign_up_ed_email);
        Button signUpBtn = findViewById(R.id.sign_up_btn);

        Button sign_up_ed_id_checkbtn = findViewById(R.id.sign_up_ed_id_checkbtn);
        Button sign_up_ed_nickname_checkbtn = findViewById(R.id.sign_up_ed_nickname_checkbtn);
        // 이메일 콤보 박스
        String[] email = {"@naver.com","@gmail.com"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, email);
        Spinner sign_up_email_spinner = findViewById(R.id.sign_up_email_spinner);
        sign_up_email_spinner.setAdapter(adapter);

        // 회원가입 창 아이디,비밀번호는 영문,숫자만 가능하게, 닉네임은 한글,영어,숫자 가능하게
        signUpId.setFilters(new InputFilter[]{IdFilter,new InputFilter.LengthFilter(12)});
        signUpNickname.setFilters(new InputFilter[]{NickNameFilter,new InputFilter.LengthFilter(10)});
        signUpPw.setFilters(new InputFilter[]{IdFilter,new InputFilter.LengthFilter(12)});
        signUpEdPwCheck.setFilters(new InputFilter[]{IdFilter,new InputFilter.LengthFilter(16)});



        // dialog
        signUpAdrress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(Sign_Up_Activity.this);

                customDialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void setViews(String _do, String siGunGu, String eupMyeonDong) {
                        signupTvDo.setText(_do);
                        signupTvSigunGu.setText(siGunGu);
                        signupTvEupMyeonDong.setText(eupMyeonDong);
                        signupTvDo.setVisibility(View.VISIBLE);
                        signupTvSigunGu.setVisibility(View.VISIBLE);
                        signupTvEupMyeonDong.setVisibility(View.VISIBLE);
                    }
                });
                customDialog.show();
            }
        });


        //회원가입 버튼
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("JSON",userEmail);
                if (idCheck == true && nicknameCheck == true && pwCheck == true && signUpEmail.getText().toString().length() > 0&&userEmail.length()>0) {
                    Request_UserVo user_vo = new Request_UserVo(signUpId.getText().toString(),
                            signUpNickname.getText().toString(),
                            signUpPw.getText().toString(),
                            signUpEmail.getText().toString()+userEmail,
                            signupTvDo.getText().toString(), signupTvSigunGu.getText().toString(), signupTvEupMyeonDong.getText().toString()
                    );
                    Gson gson = new Gson();
                    String userInfo = gson.toJson(user_vo);
                    Log.e("JSON", userInfo);
                    Call<String> call = userRetrofitInterface.signUp(user_vo);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.e("JSON", "통신 성공");
                            if (response.isSuccessful()) {
                                Log.e("JSON", response.body().toString());
                                customToastView("회원가입 완료");
                                if (response.body().toString().equals("1")) {
                                    Intent intent = new Intent(Sign_Up_Activity.this, Login_Activity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("JSON", "통신 실패");
                        }
                    });
                } else { // 순서대로 실행
                    if (idCheck == false) {
                        customToastView("ID 중복확인");
                    } else if (nicknameCheck == false) {
                        customToastView("NickName 중복확인");
                    } else if (pwCheck == false) {
                        customToastView("비밀번호를 입력하세요.");
                    } else if (signUpEmail.getText().toString().length() < 1) {
                        customToastView("이메일을 입력하세요.");
                    }
                }


            }
        });

        // 아이디 EditText 클릭시 Id 중복체크 초기화
        signUpId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sign_up_tv_duplicate_verification_id.setVisibility(View.INVISIBLE);
                idCheck = false;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //아이디 중복 체크 버튼
        sign_up_ed_id_checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signUpId.getText().toString().length() > 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", signUpId.getText().toString());
                    Call<String> call = userRetrofitInterface.idCheck(map);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.e("JSON", "통신ㅇ");
                            if (response.isSuccessful()) {
                                if (response.body().toString().equals("0") && signUpId.length()>4) {
                                    signUpNickname.requestFocus();
                                    idCheck = true;
                                    sign_up_tv_duplicate_verification_id.setText("사용가능");
                                    sign_up_tv_duplicate_verification_id.setVisibility(View.VISIBLE);
                                    sign_up_tv_duplicate_verification_id.setTextColor(Color.BLUE);
                                } else if (response.body().toString().equals("1")) {
                                    signUpId.requestFocus();
                                    idCheck = false;
                                    sign_up_tv_duplicate_verification_id.setText("중복입니다.");
                                    sign_up_tv_duplicate_verification_id.setVisibility(View.VISIBLE);
                                    sign_up_tv_duplicate_verification_id.setTextColor(Color.RED);
                                } else if (response.body().toString().equals("0") && signUpId.length()<5) {
                                    signUpId.requestFocus();
                                    idCheck = false;
                                    sign_up_tv_duplicate_verification_id.setText("ID는 5~12자의 영문 소문자, 숫자만 사용 가능합니다.");
                                    sign_up_tv_duplicate_verification_id.setVisibility(View.VISIBLE);
                                    sign_up_tv_duplicate_verification_id.setTextColor(Color.RED);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                } else {
                    Toast.makeText(Sign_Up_Activity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 닉네임 EditText 클릭시 닉네임 중복체크 초기화
        signUpNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nicknameCheck = false;
                sign_up_tv_duplicate_verification_nickname.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //닉네임 중복 체크 버튼
        sign_up_ed_nickname_checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signUpNickname.getText().toString().length() > 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put("nickName", signUpNickname.getText().toString());
                    Call<String> call = userRetrofitInterface.nickName(map);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                if (response.body().toString().equals("0")&& signUpNickname.length()>3) {
                                    signUpPw.requestFocus();
                                    nicknameCheck = true;
                                    sign_up_tv_duplicate_verification_nickname.setText("사용가능");
                                    sign_up_tv_duplicate_verification_nickname.setTextColor(Color.BLUE);
                                    sign_up_tv_duplicate_verification_nickname.setVisibility(View.VISIBLE);
                                } else if (response.body().toString().equals("1")) {
                                    signUpNickname.requestFocus();
                                    nicknameCheck = false;
                                    sign_up_tv_duplicate_verification_nickname.setText("중복입니다");
                                    sign_up_tv_duplicate_verification_nickname.setTextColor(Color.RED);
                                    sign_up_tv_duplicate_verification_nickname.setVisibility(View.VISIBLE);
                                } else if (response.body().toString().equals("0") && signUpNickname.length()<4) {
                                    signUpId.requestFocus();
                                    idCheck = false;
                                    sign_up_tv_duplicate_verification_nickname.setText("닉네임은 4~8자의 영문 소문자, 숫자, 한글만 사용 가능합니다.");
                                    sign_up_tv_duplicate_verification_nickname.setVisibility(View.VISIBLE);
                                    sign_up_tv_duplicate_verification_nickname.setTextColor(Color.RED);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {}
                    });
                } else {
                    Toast.makeText(Sign_Up_Activity.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //이메일 선택
        sign_up_email_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userEmail = email[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        signUpEdPwCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                RealTime_Thread myThread = new RealTime_Thread();
                myThread.setDaemon(true);
                if (b) { // 포커스를 가지고 있을경우
                    myThread.start();
                    threadCheck = true;
                } else { // 포커스를 잃었을 경우
                    threadCheck = false;
                }
            }
        });
    }

    // 비밀번호 일치 여부 확인
    Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                String str1 = signUpPw.getText().toString();
                String str2 = signUpEdPwCheck.getText().toString();
                if(str1.length()>7) {
                    if (str1.equals(str2)) {
                        signUpTvPwCheck.setVisibility(View.VISIBLE);
                        signUpTvPwCheck.setTextColor(Color.BLUE);
                        signUpTvPwCheck.setText("비밀번호가 일치합니다.");
                        pwCheck = true;
                    } else if (!str1.equals(str2)) {
                        signUpTvPwCheck.setVisibility(View.VISIBLE);
                        signUpTvPwCheck.setTextColor(Color.RED);
                        signUpTvPwCheck.setText("비밀번호가 일치하지 않습니다.");
                        pwCheck = false;
                    }
                }else{
                    signUpTvPwCheck.setVisibility(View.VISIBLE);
                    signUpTvPwCheck.setTextColor(Color.RED);
                    signUpTvPwCheck.setText("패스워드는 8~16자의 영문 소문자, 숫자를 사용하세요.");
                    pwCheck = false;
                }
            }
        }
    };
    class RealTime_Thread extends Thread {
        @Override
        public void run() {
            while (threadCheck) {
                try {
                    MyHandler.sendEmptyMessage(1);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //영어,숫자만
    public InputFilter IdFilter = new InputFilter(){
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dset, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]");
            if(!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };
    //영어,숫자,한글만
    public InputFilter NickNameFilter = new InputFilter(){
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dset, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+$");
            if(!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };

    public void customToastView(String text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.toast_layout) );
        TextView textview = layout.findViewById(R.id.toast);
        textview.setText(text);
        Toast toastView = Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT);
        toastView.setGravity(Gravity.CENTER,0,800);
        toastView.setView(layout);
        toastView.show();
    }
}