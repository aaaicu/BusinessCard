package com.jaehyun.businesscard.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jaehyun.businesscard.ui.businesscard.BusinessCardActivity;
import com.jaehyun.businesscard.BusinessCardApplication;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.repository.BusinessCardRepository;
import com.jaehyun.businesscard.ui.base.BasePresenter;
import com.jaehyun.businesscard.util.Injection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainPresenter extends BasePresenter implements MainContract.Presenter {

    BusinessCardRepository repository = Injection.BUSINESS_CARD_REPOSITORY;

    @Override
    public void requestBusinessCard(String id) {
        if(id.isEmpty()){
            mView.showToast("사번을 입력하세요");
        }else {
            // 데이터 요청
            repository.requestBusinessCard(id, new Callback<BusinessCardModel>() {
                @Override
                public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
                    mView.showToast("사원정보가 조회되었습니다.");

                    BusinessCardModel model = response.body();
                    if (model == null) {
                        mView.showToast("해당 사번 없음");
                    } else {
                        Log.d("test", "세션 ID " + model.toString());
                        Intent intent = new Intent(BusinessCardApplication.getAppContext(), BusinessCardActivity.class);
                        intent.putExtra("REQ", "server");
                        intent.putExtra("ID", id);
                        mView.openActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<BusinessCardModel> call, Throwable t) {
                    mView.showToast("사원정보 조회 실패");
                    Log.e("test", t.toString());
                }
            });
        }
    }

    //임시 메소드(테스트용도) 삭제 무관
    @Override
    public void requestSession(Context context) {

        repository.sessionTest(context, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences serverSessionCookie = ((AppCompatActivity) mView).getSharedPreferences("SERVER_SESSION_COOKIE", MODE_PRIVATE);
                Log.d("test", "통신");
                Log.d("test", "Response Headers : " + response.headers().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
