package com.jaehyun.businesscard.network.repository;

import android.graphics.Bitmap;

import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.network.RetrofitFactory;
import com.jaehyun.businesscard.network.service.BusinessCardService;
import com.jaehyun.businesscard.util.Config;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;

public class EmployeeRepository {
    private EmployeeRepository() {
    }

    private static class EmployeeRepositoryHolder {
        public static final EmployeeRepository INSTANCE = new EmployeeRepository();
    }

    public static EmployeeRepository getInstance(){
        return EmployeeRepositoryHolder.INSTANCE;
    }

    public Call<BusinessCardModel> getBusinessCardInfo(String seq){
        return RetrofitFactory.createJsonAdapter(Config.BASE_URL).create(BusinessCardService.class).getEmployeeBySeq(seq);
    }

    public Call<String> saveBusinessCardImage(int seq, File imageFile){
        return RetrofitFactory.createJsonAdapter(Config.BASE_URL).create(BusinessCardService.class).saveBusinessCardImage(MultipartBody.Part.createFormData("empSeq","1"), fileToMultiPart(imageFile));
    }

//    private MultipartBody.Part toMultiPartBodyPartJson(String name, String data) {
////        String encoded = Base64.encodeToString(data, Base64.NO_WRAP);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);
//        return MultipartBody.Part.createFormData(name, null, requestBody);
//    }
//    RequestBody requestFile =
//            RequestBody.create(MediaType.parse("multipart/form-data"), createFileFromBitmap(bitmap););
//    MultipartBody.Part body =
//            MultipartBody.Part.createFormData("image", makeImageFileName(), requestFile);
//
    private MultipartBody.Part fileToMultiPart (File file){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file );
        return MultipartBody.Part.createFormData("file", "businesscard", requestFile);
    }

}
