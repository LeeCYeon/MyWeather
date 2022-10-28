package com.example.myweather.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.R;

public class NewsActivity2 extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news2);

        Intent i = getIntent();
        String url = i.getStringExtra("url");

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); //클릭 시 새 창 안뜨게


        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클때 스크린크기에 맞추기
        webView.getSettings().setAllowContentAccess(false); // 웹뷰를 통해 Content URL 에 접근할지 여부


        webView.loadUrl(url);

        //todo : NewsActivity2 >> NewsFragment로 뒤로가기
    }
}