package com.jaehyun.businesscard.ui.businesscard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jaehyun.businesscard.BusinessCardApplication;
import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.customview.BusinessCardView;
import com.jaehyun.businesscard.data.local.BusinessCardEntity;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.ui.base.BaseActivity;
import com.jaehyun.businesscard.util.Config;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCardActivity extends BaseActivity implements BusinessCardContract.View {
    BusinessPresenter presenter = null;

    BusinessCardViewModel businessCardViewModel;
    BusinessCardView businessCardView = null;
    ImageView imageView = null;
    MutableLiveData<BusinessCardEntity> data = null;

    final int REQUEST_IMG_SEND = 88;
    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);
        imageView = findViewById(R.id.imageView);
        businessCardView = findViewById(R.id.businessCardView);

        presenter = new BusinessPresenter();
        presenter.setView(this);

        checkPermission();
        init();

        BusinessCardEntity entity = new BusinessCardEntity();
        hasBusinessCard(entity);
    }

    private void hasBusinessCard(BusinessCardEntity entity) {
        presenter.hasBusinessCard(this, getIntent().getStringExtra("ID"), new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("false")) {
                    // 명함 이미지가 없는 경우

                    // 데이터 가져오기
                    presenter.getBusinessCardInfo(BusinessCardApplication.getAppContext(), getIntent().getStringExtra("ID"), new Callback<BusinessCardModel>() {
                        @Override
                        public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
                            BusinessCardModel model = response.body();

                            if (model != null) {
                                entity.setId(model.getSeq());
                                entity.setName(model.getName());
                                entity.setAddress(model.getAddress());
                                entity.setEmail(model.getEmail());
                                entity.setTel(model.getTel());
                                entity.setMobile(model.getPhone());
                                entity.setTeam(model.getTeam());
                                entity.setPosition(model.getPosition());
                                entity.setFax(model.getFax());
                                Log.d("test", entity.toString() + "");

                                data.setValue(entity);
                            }
                        }

                        @Override
                        public void onFailure(Call<BusinessCardModel> call, Throwable t) {
                            Log.d("test", t.toString());
                        }
                    });

                } else {
                    getBusinessCardImage();
                }
                // 명함 이미지가 있는 경우
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("test", t.toString());
            }
        });

    }

    private void init() {
        businessCardViewModel = new BusinessCardViewModel(getApplication());
        businessCardViewModel.setEntity(new MutableLiveData<>());

        data = businessCardViewModel.getEntity();
        data.observe(this, e -> {
            businessCardView.setBusinessCardData(e);
            Bitmap bitmap = presenter.getBitmapFromView(businessCardView);

            // 서버로 이미지 전달
            if (getIntent().getStringExtra("REQ") != null) {
                if (getIntent().getStringExtra("REQ").equals("server")) {
                    Log.d("test", "서버로 이미지 저장 요청");
                    Log.d("test", getIntent().getStringExtra("ID") + "");
                    File temp = presenter.saveBitmapToPng(bitmap, "BusinessCard");

                    presenter.saveBusinessCardImage(this, getIntent().getStringExtra("ID"), temp, new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            temp.delete();
                            getBusinessCardImage();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            temp.delete();
                        }
                    });
                }
            }
        });
    }

    public void getBusinessCardImage() {
        if (getIntent().getStringExtra("REQ") != null) {
            if (getIntent().getStringExtra("REQ").equals("server")) {
                Glide.with(getApplicationContext())
                        .load(Config.BASE_URL + Config.BUSINESS_CARD_URL + getIntent().getStringExtra("ID"))
                        .apply(new RequestOptions()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(imageView);
                Log.d("test", Config.BASE_URL + Config.BUSINESS_CARD_URL + getIntent().getStringExtra("ID"));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "앱권한설정하세요", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMG_SEND) {
            presenter.deleteTempFile();
        }
    }

    @Override
    public void sendBusinessCard(View view) {
        presenter.sendBusinessCard();
    }

    @Override
    public void checkPermission() {
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for (String permission : permission_list) {
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if (chk == PackageManager.PERMISSION_DENIED) {
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list, 0);
            }
        }
    }

    public void requestSend(View view) {
        SendBusinessCardModel model = new SendBusinessCardModel();
        model.setContent("내용");
        model.setReceiver("받는이");
        model.setSender(Integer.parseInt(getIntent().getStringExtra("ID")));
        model.setSendType("kakao");

        presenter.sendBusinessCard(model, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(), "메시지 요청 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void sendMMS(Intent chooser) {

        startActivityForResult(chooser, REQUEST_IMG_SEND);
    }

//    private void observeRoomDB(int empId){
//        BusinessCardApplication.getDatabase()
//                .businessCardDao()
//                .getBusinessCard(empId)
//                .observe(this, businessCardEntity -> {
//                    if (businessCardEntity != null){
//                        Log.d("test", "관찰 테스트" + businessCardEntity.toString());
//                        data.setValue(businessCardEntity);
//                    }
//        });
//    }
}
