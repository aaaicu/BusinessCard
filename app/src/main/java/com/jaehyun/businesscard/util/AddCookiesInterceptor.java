package com.jaehyun.businesscard.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.jaehyun.businesscard.BusinessCardApplication;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor {

//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request.Builder builder = chain.request().newBuilder();
// Preference에서 cookies를 가져오는 작업을 수행

//        SharedPreferences.Editor editor = BusinessCardApplication.getAppContext().getSharedPreferences("SERVER_SESSION_COOKIE", Activity.MODE_PRIVATE).edit();
//        editor.pu
//        Set<String> preferences = SharedPreferenceBase.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_COOKIE, new HashSet<String>());
//        for (String cookie : preferences) {
//            builder.addHeader("Cookie", cookie);
//        }

// Web,Android,iOS 구분을 위해 User-Agent세팅

//        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");
//        return chain.proceed(builder.build());
//    }


}
