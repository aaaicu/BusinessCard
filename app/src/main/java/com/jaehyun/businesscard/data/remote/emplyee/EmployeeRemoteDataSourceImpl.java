package com.jaehyun.businesscard.data.remote.emplyee;

import android.content.Context;

import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.network.RetrofitFactory;
import com.jaehyun.businesscard.network.BusinessCardService;
import com.jaehyun.businesscard.util.Config;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EmployeeRemoteDataSourceImpl implements EmployeeRemoteDataSource {
    private EmployeeRemoteDataSourceImpl() {
    }

    private static class EmployeeDataSourceHolder {
        public static final EmployeeRemoteDataSourceImpl INSTANCE = new EmployeeRemoteDataSourceImpl();
    }

    @Override
    public EmployeeRemoteDataSource getInstance() {
        return EmployeeDataSourceHolder.INSTANCE;
    }

    @Override
    public Call<BusinessCardModel> getBusinessCardInfo(Context context, String seq) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).getEmployeeBySeq(seq);
    }

    @Override
    public Call<String> hasBusinessCard(Context context, String seq) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).hasBusinessCard(seq);
    }

    @Override
    public Call<Void> sendBusinessCard(Context context, SendBusinessCardModel requestModel) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class).sendBusinessCard(requestModel);
    }

    @Override
    public Call<String> saveBusinessCardImage(Context context, String seq, File imageFile) {
        return RetrofitFactory.createJsonAdapter(context, Config.BASE_URL).create(BusinessCardService.class)
                .saveBusinessCardImage(
                        MultipartBody.Part.createFormData("seq", seq),
                        fileToMultiPart("businesscard.png", imageFile)
                );
    }

    private MultipartBody.Part fileToMultiPart(String filename, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", filename, requestFile);
    }
}
