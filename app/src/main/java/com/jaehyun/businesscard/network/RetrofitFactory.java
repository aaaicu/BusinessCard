package com.jaehyun.businesscard.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jaehyun.businesscard.BusinessCardApplication;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RETROFIT 초기화 및 생성
 * CONNECT TIMEOUT, READ TIMEOUT 설정
 */
public class RetrofitFactory {

    public static Retrofit createJsonAdapter(Context context, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();

        return retrofit;
    }

    private static OkHttpClient createOkHttpClient() {

//        SelfSigningHelper helper = SelfSigningHelper.getInstance();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(180, TimeUnit.SECONDS);
        builder.readTimeout(180, TimeUnit.SECONDS);
//        helper.setSSLOkHttp(builder, "10.0.2.2");


//        String session = sp.getString("session","");
//        Log.d("test", "세션 요청 :"+ session);

//        builder.addInterceptor(chain -> {
//            final Request original = chain.request();
//
//            final Request authorized = original.newBuilder()
//                    .addHeader("JSESSIONID", session+123)
//                    .addHeader("Cookie", "JSESSIONID="+session+123+"; Path=/; Secure; HttpOnly")
//                    .build();
//
//            return chain.proceed(authorized);
//        });

        builder.addInterceptor(chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                SharedPreferences sp = BusinessCardApplication.getAppContext().getSharedPreferences("SERVER_SESSION_COOKIE", Activity.MODE_PRIVATE);
                sp.edit().putStringSet("session", cookies).apply();
                Log.d("test", "받은 쿠키  :" + cookies);
            }
            return originalResponse;
        });
        builder.addInterceptor(chain -> {
            Request.Builder builder1 = chain.request().newBuilder();

            SharedPreferences sp = BusinessCardApplication.getAppContext().getSharedPreferences("SERVER_SESSION_COOKIE", Activity.MODE_PRIVATE);
            Log.d("test", "보낼때 세팅한 쿠키  :" +sp.getStringSet("session",new HashSet<>()));
            Set<String> preferences = sp.getStringSet("session", new HashSet<>());
            for (String cookie : preferences) {
                builder1.addHeader("Cookie", cookie);
                Log.d("test", "보낼때 세팅한 쿠키  :" + cookie);
            }
            builder1.removeHeader("User-Agent").addHeader("User-Agent", "Android");
            return chain.proceed(builder1.build());
        });

        return builder.build();
    }
}