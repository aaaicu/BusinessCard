package com.jaehyun.businesscard.network.repository;

import com.google.gson.Gson;
import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.model.SendBusinessCardModel;
import com.jaehyun.businesscard.network.RetrofitFactory;
import com.jaehyun.businesscard.network.service.BusinessCardService;
import com.jaehyun.businesscard.util.Config;

import java.io.File;

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

    public Call<BusinessCardModel> getBusinessCardInfo(String seq){
        return RetrofitFactory.createJsonAdapter(Config.BASE_URL).create(BusinessCardService.class).getEmployeeBySeq(seq);
    }

    public Call<String> hasBusinessCard(String seq) {
        return RetrofitFactory.createJsonAdapter(Config.BASE_URL).create(BusinessCardService.class).hasBusinessCard(seq);
    }

//    public Call<String> sendBusinessCard(SendBusinessCardModel requestModel) {
//        Gson gson = new Gson();
//        String request = gson.toJson(requestModel ,SendBusinessCardModel.class );
//
//        return RetrofitFactory.createJsonAdapter(Config.BASE_URL).create(BusinessCardService.class).sendBusinessCard(request);
//    }

    public Call<String> saveBusinessCardImage(String seq, File imageFile){
        return RetrofitFactory.createJsonAdapter(Config.BASE_URL).create(BusinessCardService.class)
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
