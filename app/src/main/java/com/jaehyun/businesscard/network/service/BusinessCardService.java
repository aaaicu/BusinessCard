package com.jaehyun.businesscard.network.service;

import android.graphics.Bitmap;

import com.jaehyun.businesscard.model.BusinessCardModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BusinessCardService {

    @GET("/employee/info")
    Call<BusinessCardModel> getEmployeeBySeq(@Query("seq") String param);

    @Multipart
    @POST("/employee/businesscard")
    Call<String> saveBusinessCardImage(
            @Part MultipartBody.Part seq,
            @Part MultipartBody.Part file);

}
