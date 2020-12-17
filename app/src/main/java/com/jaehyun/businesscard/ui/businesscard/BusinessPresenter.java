package com.jaehyun.businesscard.ui.businesscard;

import android.content.Context;

import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.data.repository.BusinessCardRepository;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.ui.base.BasePresenter;

import java.io.File;

import retrofit2.Callback;

public class BusinessPresenter extends BasePresenter implements BusinessCardContract.Presenter {

    // datasource 셋팅하는거 필요
    BusinessCardRepository repository;

    @Override
    public void hasBusinessCard(Context context, String seq, Callback<String> callback) {
        repository.hasBusinessCard(context, seq, callback);
    }

    @Override
    public void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback) {
        repository.getBusinessCardInfo(context, id, callback);
    }

    @Override
    public void saveBusinessCardImage(Context context, String seq, File file, Callback<String> callback) {
        repository.saveBusinessCardImage(context, seq, file, callback);
    }

    @Override
    public void sendBusinessCard(Context context, SendBusinessCardModel model, Callback<Void> callback) {
        repository.sendBusinessCard(context, model, callback);
    }
}
