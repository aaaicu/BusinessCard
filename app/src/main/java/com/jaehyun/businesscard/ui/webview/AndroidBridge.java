package com.jaehyun.businesscard.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.jaehyun.businesscard.data.repository.BusinessCardRepository;
import com.jaehyun.businesscard.ui.businesscard.BusinessCardActivity;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.remote.emplyee.EmployeeRemoteDataSourceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AndroidBridge implements WebViewContract.Presenter {
    Context mContext;
    // datasource 셋팅하는거 필요
    BusinessCardRepository repository;

    public AndroidBridge(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void callNative(String id) {

        repository.getBusinessCardInfo(mContext, id, new Callback<BusinessCardModel>() {

            @Override
            public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
                Toast.makeText(mContext, "사원정보가 조회되었습니다.", Toast.LENGTH_SHORT).show();
                BusinessCardModel model = response.body();
                if (model == null) {
                    Toast.makeText(mContext, "해당 사번 없음", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(mContext, BusinessCardActivity.class);
                    intent.putExtra("REQ", "server");
                    intent.putExtra("ID", id.toString());

                    mContext.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<BusinessCardModel> call, Throwable t) {
                Log.d("test", t.toString());
            }
        });
    }
}
