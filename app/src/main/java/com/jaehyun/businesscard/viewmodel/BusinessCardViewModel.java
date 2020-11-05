package com.jaehyun.businesscard.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jaehyun.businesscard.database.entity.BusinessCardEntity;

public class BusinessCardViewModel extends AndroidViewModel {
    private MutableLiveData<BusinessCardEntity> mEntity;

    public BusinessCardViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<BusinessCardEntity> getEntity() {

        return mEntity;
    }

    public void setEntity(MutableLiveData<BusinessCardEntity> mEntity) {
        this.mEntity = mEntity;
    }
}
