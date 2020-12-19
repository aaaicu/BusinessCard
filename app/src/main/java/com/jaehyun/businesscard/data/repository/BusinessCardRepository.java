package com.jaehyun.businesscard.data.repository;

import android.content.Context;

import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.data.remote.emplyee.EmployeeRemoteDataSource;
import com.jaehyun.businesscard.data.remote.session.SessionRemoteDataSource;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public interface BusinessCardRepository {

    void setEmployeeDataSource(EmployeeRemoteDataSource dataSource);

    void setSessionDataSource(SessionRemoteDataSource dataSource);

    void requestBusinessCard(String id, Callback<BusinessCardModel> callback);

    void sessionTest(Context context, Callback<ResponseBody> callback);

    void hasBusinessCard(Context context, String seq, Callback<String> callback);

    void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback);

    void saveBusinessCardImage(Context context, String seq, File file, Callback<String> callback);

    void sendBusinessCard(Context context, SendBusinessCardModel model, Callback<Void> callback);
}
