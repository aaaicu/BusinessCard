package com.jaehyun.businesscard.ui.webview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.ui.base.BaseActivity;
import com.jaehyun.businesscard.ui.webview.AndroidBridge;
import com.jaehyun.businesscard.util.Config;
import com.jaehyun.businesscard.network.ssl.SelfSigningHelper;

import java.io.IOException;
import java.net.URL;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebViewActivity extends BaseActivity implements WebViewContract.View {
    private OkHttpClient.Builder builder = null;
    WebView webView = null;
    SelfSigningHelper helper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        builder = new OkHttpClient.Builder();
//        helper = SelfSigningHelper.getInstance();
//        helper.setSSLOkHttp(builder,"10.0.2.2");

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
        webView.setWebViewClient(new WebViewClient(){


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request){
                URL url;
                Response response;
                Request newRequest;
                try {
                    url = new URL(request.getUrl().toString());
                    newRequest = new Request.Builder().url(url).build();
                    response = builder.build().newCall(newRequest).execute();
                    if (response.body() != null) {
                        return new WebResourceResponse("","",response.body().byteStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Deprecated
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.d("test","deprecate");
                Request okHttpRequest = new Request.Builder().url(url).build();
                try {

                    Response response = builder.build().newCall(okHttpRequest).execute();
                    return new WebResourceResponse("","",response.body().byteStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        webView.clearCache(true);
        webView.loadUrl(Config.BASE_URL);

    }
}
