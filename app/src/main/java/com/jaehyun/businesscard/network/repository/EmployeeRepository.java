package com.jaehyun.businesscard.network.repository;

import android.content.Context;

import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.model.SendBusinessCardModel;
import com.jaehyun.businesscard.network.RetrofitFactory;
import com.jaehyun.businesscard.network.service.BusinessCardService;
import com.jaehyun.businesscard.util.Config;

import java.io.File;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EmployeeRepository {
    private EmployeeRepository() {
    }

    private static class EmployeeRepositoryHolder {
        public static final EmployeeRepository INSTANCE = new EmployeeRepository();
    }

    public static EmployeeRepository getInstance(){
        return EmployeeRepositoryHolder.INSTANCE;
    }

    public Call<BusinessCardModel> getBusinessCardInfo(Context context, String seq){
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).getEmployeeBySeq(seq);
    }

    public Call<String> hasBusinessCard(Context context, String seq) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).hasBusinessCard(seq);
    }

    public Call<Void> sendBusinessCard(Context context, SendBusinessCardModel requestModel) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).sendBusinessCard(requestModel);
    }

    public Call<String> saveBusinessCardImage(Context context, String seq, File imageFile){
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class)
                .saveBusinessCardImage(
                        MultipartBody.Part.createFormData("seq",seq),
                        fileToMultiPart("businesscard.png", imageFile)
                );
    }

    private MultipartBody.Part fileToMultiPart (String filename , File file){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file );
        return MultipartBody.Part.createFormData("file", filename, requestFile);
    }

}
