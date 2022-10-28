package com.example.myweather.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.myweather.R;

public class MainMenuNewsFragment2 extends Fragment {
    ViewGroup viewGroup;
    WebView webView;
    WebSettings webSettings;
    String url;
    ImageView undo;

    public MainMenuNewsFragment2() {
        // Required empty public constructor
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//
//        return 0;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup= (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_news2, container,false);

        webView = (WebView)viewGroup.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        undo= (ImageView)viewGroup.findViewById(R.id.iv_undo);


        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클때 스크린크기에 맞추기
        webView.getSettings().setAllowContentAccess(false); // 웹뷰를 통해 Content URL 에 접근할지 여부

        if (getArguments() != null)
        {

            url= getArguments().getString("url");

            webView.loadUrl(url);
        }


        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //코드 상에서 띄운 프래그먼트를 지우고 스택에 저장된 프래그먼트 띄우기
                getActivity().getSupportFragmentManager().beginTransaction().remove(MainMenuNewsFragment2.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return viewGroup;
    }
}
