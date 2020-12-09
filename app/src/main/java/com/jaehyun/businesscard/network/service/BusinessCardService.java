package com.jaehyun.businesscard.network.service;

import android.graphics.Bitmap;

import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.model.SendBusinessCardModel;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BusinessCardService {

    @GET("/employee/info")
    Call<BusinessCardModel> getEmployeeBySeq(@Query("seq") String param);

    @GET("/employee/hasBusinessCard")
    Call<String> hasBusinessCard(@Query("seq") String seq);

    @Multipart
    @POST("/employee/businessCard")
    Call<String> saveBusinessCardImage(
            @Part MultipartBody.Part seq ,
            @Part MultipartBody.Part file
    );
    @HTTP(method = "POST", path = "/send/businessCard", hasBody = true)
    Call<Void> sendBusinessCard(@Body SendBusinessCardModel requestModel);

    @GET("/test/session")
    Call<ResponseBody> testSession();
}
