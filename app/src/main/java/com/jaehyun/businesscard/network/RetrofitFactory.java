package com.jaehyun.businesscard.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RETROFIT 초기화 및 생성
 * CONNECT TIMEOUT, READ TIMEOUT 설정
 */
public class RetrofitFactory {

    public static Retrofit createJsonAdapter(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
        return retrofit;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(180, TimeUnit.SECONDS);
        builder.readTimeout(180, TimeUnit.SECONDS);
        return builder.build();
    }
}
