package com.jaehyun.businesscard.data.remote.emplyee;

import android.content.Context;

import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;

import java.io.File;

import retrofit2.Call;

public interface EmployeeRemoteDataSource{
    Call<BusinessCardModel> getBusinessCardInfo(Context context, String seq);

    Call<String> hasBusinessCard(Context context, String seq);

    Call<Void> sendBusinessCard(Context context, SendBusinessCardModel requestModel);

    Call<String> saveBusinessCardImage(Context context, String seq, File imageFile);
}
