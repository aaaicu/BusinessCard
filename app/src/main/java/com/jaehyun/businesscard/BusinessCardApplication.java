package com.jaehyun.businesscard;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.RoomDatabase;

import com.jaehyun.businesscard.database.BusinessDB;

public class BusinessCardApplication extends Application {

    private static BusinessCardApplication application;
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Log.d("test","APP 실행");
        mAppExecutors = new AppExecutors();

    }

    public static Context getAppContext() {
        return application.getApplicationContext();
    }

    public static BusinessDB getDatabase(){
        return BusinessDB.getsInstance();
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }
}
