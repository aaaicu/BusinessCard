package com.jaehyun.businesscard.data.remote.session;

import android.content.Context;

import com.jaehyun.businesscard.network.RetrofitFactory;
import com.jaehyun.businesscard.network.BusinessCardService;
import com.jaehyun.businesscard.util.Config;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SessionRemoteDataSourceImpl implements SessionRemoteDataSource {
    private SessionRemoteDataSourceImpl() {
    }

    private static class SessionDataSourceHolder {
        public static final SessionRemoteDataSourceImpl INSTANCE = new SessionRemoteDataSourceImpl();
    }

    @Override
    public SessionRemoteDataSource getInstance() {
        return SessionDataSourceHolder.INSTANCE;
    }

    @Override
    public Call<ResponseBody> sessionTest(Context context) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).testSession();
    }
}
