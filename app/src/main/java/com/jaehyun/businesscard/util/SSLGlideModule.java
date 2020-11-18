package com.jaehyun.businesscard.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.jaehyun.businesscard.BusinessCardApplication;

import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public class SSLGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);

        OkHttpClient okHttpClient=SelfSigningClientBuilder.addBuilder(BusinessCardApplication.getAppContext(),new OkHttpClient.Builder()).build();
//        OkHttpClient okHttpClient=new OkHttpClient();

        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}