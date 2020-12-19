package com.jaehyun.businesscard.util;

import com.jaehyun.businesscard.data.remote.emplyee.EmployeeRemoteDataSource;
import com.jaehyun.businesscard.data.remote.emplyee.EmployeeRemoteDataSourceImpl;
import com.jaehyun.businesscard.data.remote.session.SessionRemoteDataSource;
import com.jaehyun.businesscard.data.remote.session.SessionRemoteDataSourceImpl;
import com.jaehyun.businesscard.data.repository.BusinessCardRepository;
import com.jaehyun.businesscard.data.repository.BusinessCardRepositoryImpl;

public class Injection {
    public static final BusinessCardRepository BUSINESS_CARD_REPOSITORY = new BusinessCardRepositoryImpl(EmployeeRemoteDataSourceImpl.getInstance(), SessionRemoteDataSourceImpl.getInstance());

}
