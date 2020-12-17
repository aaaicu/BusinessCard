package com.jaehyun.businesscard.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jaehyun.businesscard.BusinessCardApplication;

@Database(entities = {BusinessCardEntity.class},version = 1,exportSchema = false)
public abstract class BusinessDB extends RoomDatabase {
    private static final String DATABASE_NAME = "business-card-db";

    public abstract BusinessCardDao businessCardDao();

    public static BusinessDB getsInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final BusinessDB INSTANCE = initialize();
    }
    private static BusinessDB initialize() {
//        CuSafeHelperFactory factory = new CuSafeHelperFactory();
//        return buildDataBase(CuApp.getAppContext(), factory.create(CuApp.getAppContext()));
    return Room.databaseBuilder(BusinessCardApplication.getAppContext(), BusinessDB.class, DATABASE_NAME).allowMainThreadQueries().build();
    }
}
