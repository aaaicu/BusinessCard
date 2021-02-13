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
import com.jaehyun.businesscard.customview.VisibilityOptionView;
import com.jaehyun.businesscard.data.local.BusinessCardEntity;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.ui.base.BaseActivity;
import com.jaehyun.businesscard.util.Config;

import java.io.File;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCardActivity extends BaseActivity implements BusinessCardContract.View {
    final int REQUEST_IMG_SEND = 88;

    BusinessPresenter presenter = null;

    BusinessCardViewModel businessCardViewModel;
    BusinessCardView businessCardView = null;
    VisibilityOptionView visibilityOptionView = null;
    ImageView imageView = null;

    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        1. 아무것도 없는 View 를 배치
        2. 인사정보를 가져옴
        3. 인사정보를 명함 뷰에 파싱

        4. 별도로 이미지로 요청할 수 있도록 만듦
        5. 서버에 이미지가 존재하는지 체크하는 로직은 잠시 주석처리 후 필요할 때 오픈

        * */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);
        imageView = findViewById(R.id.imageView);
        businessCardView = findViewById(R.id.businessCardView);
        businessCardViewModel = new ViewModelProvider(this).get(BusinessCardViewModel.class);

        visibilityOptionView = findViewById(R.id.visibility_option);
        visibilityOptionView.setBusinessCardView(businessCardView);

        presenter = new BusinessPresenter();
        presenter.setView(this);
        checkPermission();

        // 데이터 변경을 알려줌
        presenter.getChangeEmpData(getIntent().getStringExtra("ID"));

    }

    /**
     * presenter 에서 사용
     * 데이터가 바뀔경우 뷰의화면을 바꾸기위해사용
     */
    @Override
    public void changeBusinessCardView(BusinessCardEntity entity) {
        businessCardView.setBusinessCardData(entity);
    }

    /**
     * 서버에 명함 전송 요청보내기 (공유하기)
     */
    public void requestSend(View view) {
        //샘플 형식 - 화면에서 데이터를 받아 객체로 전달
        SendBusinessCardModel model = new SendBusinessCardModel();
        model.setContent("내용");
        model.setReceiver("받는이");
        model.setSender(Integer.parseInt(getIntent().getStringExtra("ID")));
        model.setSendType("kakao");

        presenter.sendBusinessCard(model);
    }

    /**
     * MMS 로 명함 직접 보내기 (공유하기)
     */
    @Override
    public void sendBusinessCard(View view) {
        presenter.sendBusinessCard();
    }

    @Override
    public void sendMMS(Intent chooser) {
        startActivityForResult(chooser, REQUEST_IMG_SEND);
    }


    /**
     * 앱권한 요청
     */
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

    /**
     * 앱권한 요청
     */
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

    /**
     * 임시 저장한 파일 삭제
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMG_SEND) {
            presenter.deleteTempBusinessCardFile();
        }
    }

//    MutableLiveData<BusinessCardEntity> data = null;



//    private void hasBusinessCard(BusinessCardEntity entity) {
//        presenter.hasBusinessCard(this, getIntent().getStringExtra("ID"));
//
//    }

//    private void init() {
//        businessCardViewModel = new ViewModelProvider(this).get(BusinessCardViewModel.class);

//        businessCardViewModel = new BusinessCardViewModel(getApplication());
//        businessCardViewModel.setEntity(new MutableLiveData<>());

//        data = businessCardViewModel.getEntity();
//        data.observe(this, e -> {
//            businessCardView.setBusinessCardData(e);
//            Bitmap bitmap = presenter.getBitmapFromView(businessCardView);
//
//            // 서버로 이미지 전달
//            if (getIntent().getStringExtra("REQ") != null) {
//                if (getIntent().getStringExtra("REQ").equals("server")) {
//                    Log.d("test", "서버로 이미지 저장 요청");
//                    Log.d("test", getIntent().getStringExtra("ID") + "");
//                    File temp = presenter.saveBitmapToPng(bitmap, "BusinessCard");
//
//                    presenter.saveBusinessCardImage(this, getIntent().getStringExtra("ID"), temp, new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            temp.delete();
//                            getBusinessCardImage();
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//                            temp.delete();
//                        }
//                    });
//                }
//            }
//        });
//    }

//    public void getBusinessCardImage() {
//        if (getIntent().getStringExtra("REQ") != null) {
//            if (getIntent().getStringExtra("REQ").equals("server")) {
//                Glide.with(getApplicationContext())
//                        .load(Config.BASE_URL + Config.BUSINESS_CARD_URL + getIntent().getStringExtra("ID"))
//                        .apply(new RequestOptions()
//                                .skipMemoryCache(true)
//                                .diskCacheStrategy(DiskCacheStrategy.NONE))
//                        .into(imageView);
//                Log.d("test", Config.BASE_URL + Config.BUSINESS_CARD_URL + getIntent().getStringExtra("ID"));
//            }
//        }
//    }





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
