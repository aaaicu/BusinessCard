package com.jaehyun.businesscard.network;

import android.content.Context;
import android.util.Log;

import com.jaehyun.businesscard.util.SelfSigningHelper;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RETROFIT 초기화 및 생성
 * CONNECT TIMEOUT, READ TIMEOUT 설정
 */
public class RetrofitFactory {

    public static Retrofit createJsonAdapter(Context context,String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
        return retrofit;
    }

    private static OkHttpClient createOkHttpClient() {
        SelfSigningHelper helper = SelfSigningHelper.getInstance();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(180, TimeUnit.SECONDS);
        builder.readTimeout(180, TimeUnit.SECONDS);
        helper.setSSLOkHttp( builder,"10.0.2.2");

        return builder.build();
    }
}