package com.example.myweather.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myweather.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FindMenuIDFragment extends Fragment {

    ViewGroup viewGroup;
    TextView result_id, verification_eamil;
    EditText et_email;
    Spinner email_spiner;
    Button email_btn_input;
    public FindMenuIDFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_find_menu_id, container, false);

        result_id = viewGroup.findViewById(R.id.find_id_show_id);
        et_email = viewGroup.findViewById(R.id.sign_up_ed_email);
        email_spiner = viewGroup.findViewById(R.id.find_id_email_spinner);
        email_btn_input= viewGroup.findViewById(R.id.find_id_input_btn);
        verification_eamil= viewGroup.findViewById(R.id.find_id_tv_verification_email);


//        Bundle bundle = getArguments();
//        JSONObject json = null;
//        try {
//            json = new JSONObject(bundle.getString("data"));
//            String str= json.get("email").toString();
//            Log.i("test",str);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        Log.i("JSON", "mypage : " + bundle);
//        try {
//            json = new JSONObject(bundle.getString("data"));
//
//            result_id.setText(json.get("email").toString());
//            Log.i("JSON", "data"+json.toString());
//        } catch (JSONException e) {
//            Log.i("JSON", "변환 에러 : " + e.toString());
//            e.printStackTrace();
//        }

        return viewGroup;
    }
}
