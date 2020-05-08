package com.rumooursindoyo.moheeeetgupta;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class global extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_global);
        WebView webView=(WebView)findViewById (R.id.web_global);
        WebSettings webSettings=webView.getSettings ();
        webSettings.setJavaScriptEnabled (true);
        webView.setWebViewClient (new WebViewClient ());
        webView.loadUrl ("http://kanchn.ml/");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed ();
    }
    }

