package com.jaehyun.businesscard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jaehyun.businesscard.util.AndroidBridge;
import com.jaehyun.businesscard.util.SelfSigningClientBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebViewActivity extends AppCompatActivity {
    private OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
    private OkHttpClient okHttp = null;
    WebView webView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        okHttp = SelfSigningClientBuilder.addBuilder(this, okHttpBuilder).build();

        webView = findViewById(R.id.webView);
        initWebView();

    }
    private void initWebView(){


        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
        webView.getSettings().setSupportZoom(false); // 줌설정
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 캐시설정
        webView.getSettings().setDefaultTextEncodingName("UTF-8"); // 인코딩 설정
        webView.addJavascriptInterface(new AndroidBridge(this), "AndroidApp");


//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Request okHttpRequest = new Request.Builder().url(url).build();
                try {
                    Response response = okHttp.newCall(okHttpRequest).execute();
                    return new WebResourceResponse("","",response.body().byteStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        webView.clearCache(true);
        webView.loadUrl("https://10.0.2.2:8443");

    }
}
