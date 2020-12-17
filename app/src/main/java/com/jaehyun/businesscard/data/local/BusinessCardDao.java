package com.jaehyun.businesscard.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BusinessCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusinessCard(BusinessCardEntity businessCardEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateBusinessCard(BusinessCardEntity businessCardEntity);

    @Delete
    int deleteBusinessCard(BusinessCardEntity businessCardEntity);

    @Query("SELECT * from business_card WHERE id =:id limit 1")
    LiveData<BusinessCardEntity> getBusinessCard(int id);

    @Query("SELECT count(*) from business_card")
    int getCountCard();
}
