package com.jaehyun.businesscard.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jaehyun.businesscard.database.entity.BusinessCardEntity;

public class BusinessCardViewModel extends AndroidViewModel {
    private LiveData<BusinessCardEntity> mEntity;

    public BusinessCardViewModel(@NonNull Application application) {
        super(application);


    }

    public LiveData<BusinessCardEntity> getEntity() {

        return mEntity;
    }

    public void setEntity(LiveData<BusinessCardEntity> mEntity) {
        this.mEntity = mEntity;
    }
}
