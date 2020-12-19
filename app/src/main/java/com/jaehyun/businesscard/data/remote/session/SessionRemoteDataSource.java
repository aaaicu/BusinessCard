package com.jaehyun.businesscard.data.remote.session;

import android.content.Context;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface SessionRemoteDataSource {
    Call<ResponseBody> sessionTest(Context context);
}
