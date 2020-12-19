package com.jaehyun.businesscard.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jaehyun.businesscard.BusinessCardApplication;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.data.remote.emplyee.EmployeeRemoteDataSource;
import com.jaehyun.businesscard.data.remote.session.SessionRemoteDataSource;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class BusinessCardRepositoryImpl implements BusinessCardRepository {
    EmployeeRemoteDataSource employeeDataSource = null;
    SessionRemoteDataSource sessionDataSource = null;

    private BusinessCardRepositoryImpl() {
        super();
    }

    public BusinessCardRepositoryImpl(@NonNull EmployeeRemoteDataSource employeeDataSource,
                                      @NonNull SessionRemoteDataSource sessionDataSource) {
        setEmployeeDataSource(employeeDataSource);
        setSessionDataSource(sessionDataSource);
    }

    @Override
    public void setEmployeeDataSource(@NonNull EmployeeRemoteDataSource dataSource) {
        if (employeeDataSource == null) {
            employeeDataSource = dataSource;
        } else {
            try {
                throw new Exception("EmployeeRemoteDataSource 없음");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setSessionDataSource(SessionRemoteDataSource dataSource) {
        if (sessionDataSource == null) {
            sessionDataSource = dataSource;
        } else {
            try {
                throw new Exception("SessionRemoteDataSource 없음");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void requestBusinessCard(String id, Callback<BusinessCardModel> callback) {
        checkDataSource(employeeDataSource == null, "EmployeeRemoteDataSource 없음");
        Log.d("test",employeeDataSource+"");
        employeeDataSource.getBusinessCardInfo(BusinessCardApplication.getAppContext(), id).enqueue(callback);
    }

    @Override
    public void sessionTest(Context context, Callback<ResponseBody> callback) {
        checkDataSource(sessionDataSource == null, "SessionDataSource 없음");
        sessionDataSource.sessionTest(context).enqueue(callback);
    }

    @Override
    public void hasBusinessCard(Context context, String seq, Callback<String> callback) {
        checkDataSource(employeeDataSource == null, "EmployeeRemoteDataSource 없음");
        employeeDataSource.hasBusinessCard(context, seq).enqueue(callback);
    }

    @Override
    public void getBusinessCardInfo(Context context, String seq, Callback<BusinessCardModel> callback) {
        checkDataSource(employeeDataSource == null, "EmployeeRemoteDataSource 없음");
        employeeDataSource.getBusinessCardInfo(context, seq).enqueue(callback);
    }

    @Override
    public void saveBusinessCardImage(Context context, String seq, File file, Callback<String> callback) {
        checkDataSource(employeeDataSource == null, "EmployeeRemoteDataSource 없음");
        employeeDataSource.saveBusinessCardImage(context, seq, file).enqueue(callback);
    }

    @Override
    public void sendBusinessCard(Context context, SendBusinessCardModel model, Callback<Void> callback) {
        checkDataSource(employeeDataSource == null, "EmployeeRemoteDataSource 없음");
        employeeDataSource.sendBusinessCard(context,model).enqueue(callback);
    }

    private void checkDataSource(boolean b, String s) {
        if (b) {
            try {
                throw new Exception(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
