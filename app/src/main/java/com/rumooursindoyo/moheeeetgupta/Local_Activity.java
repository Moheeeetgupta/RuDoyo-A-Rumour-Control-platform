package com.rumooursindoyo.moheeeetgupta;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class Local_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_local_);

        WebView webView=(WebView)findViewById (R.id.web_local);
        WebSettings webSettings=webView.getSettings ();
        webSettings.setJavaScriptEnabled (true);
        webView.setWebViewClient (new WebViewClient ());
        webView.loadUrl ("https://surveyheart.com/form/5e85ade1e5c354636643c53d");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed ();
    }
}


