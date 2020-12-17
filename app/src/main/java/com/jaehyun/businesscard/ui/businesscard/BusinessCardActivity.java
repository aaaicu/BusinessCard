package com.jaehyun.businesscard.ui.businesscard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jaehyun.businesscard.BusinessCardApplication;
import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.data.local.BusinessCardEntity;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.data.remote.emplyee.EmployeeRemoteDataSourceImpl;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.ui.base.BaseActivity;
import com.jaehyun.businesscard.util.Config;
import com.jaehyun.businesscard.customview.BusinessCardView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCardActivity extends BaseActivity implements BusinessCardContract.View {
    BusinessPresenter presenter = null;

    BusinessCardViewModel businessCardViewModel;
    BusinessCardView businessCardView = null;
    ImageView imageView = null;
    MutableLiveData<BusinessCardEntity> data = null;
    Bitmap bitmap = null;
    File tempFile = null;

    final int REQUEST_IMG_SEND = 88;
    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);
        presenter = new BusinessPresenter();
        presenter.setView(this);

        checkPermission();

        imageView = findViewById(R.id.imageView);
        businessCardView = findViewById(R.id.businessCardView);

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
            Bitmap bitmap = getBitmapFromView(businessCardView);

            // 서버로 이미지 전달
            if (getIntent().getStringExtra("REQ") != null) {
                if (getIntent().getStringExtra("REQ").equals("server")) {
                    Log.d("test", "서버로 이미지 저장 요청");
                    Log.d("test", getIntent().getStringExtra("ID") + "");
                    File temp = saveBitmapToPng(bitmap, "BusinessCard");

                    presenter.saveBusinessCardImage(this,getIntent().getStringExtra("ID"), temp, new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            temp.delete();
                            getBusinessCardImage();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            temp.delete();
                        }
                    } );
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
            tempFile.deleteOnExit();
        }
    }

    public Bitmap getBitmapFromView(View v) {
        if (bitmap == null) {
            View temp = findViewById(R.id.businessCard);
            bitmap = Bitmap.createBitmap(temp.getMeasuredWidth(), temp.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private File saveBitmapToPng(Bitmap bitmap, String name) {

        try {
            File tempDir = getCacheDir();
            tempFile = File.createTempFile("BUSINESS_CARD_PNG_", ".png", tempDir);
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("test", tempFile.getAbsolutePath() + "이미지 다운로드");
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("test", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("test", "IOException : " + e.getMessage());
        }
        return tempFile;
    }

    public void sendBusinessCard(View view) {
        if (imageView != null) {
            saveBitmapToPng(((BitmapDrawable) imageView.getDrawable()).getBitmap(), "BusinessCard");
            sendMMS(getUri(tempFile));
        }
    }

    private void sendMMS(Uri uri) {

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, "send"), REQUEST_IMG_SEND);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getUri(File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// API 24 이상 일경우..
            uri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".fileprovider", file);
        } else {// API 24 미만 일경우..
            uri = Uri.fromFile(file);
        }
        return uri;
    }

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

        presenter.sendBusinessCard(getApplicationContext(), model, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(), "메시지 요청 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
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
