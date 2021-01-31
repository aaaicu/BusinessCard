package com.jaehyun.businesscard.ui.businesscard;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.jaehyun.businesscard.BusinessCardApplication;
import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.data.local.BusinessCardEntity;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.data.repository.BusinessCardRepository;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.ui.base.BasePresenter;
import com.jaehyun.businesscard.util.Injection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessPresenter extends BasePresenter implements BusinessCardContract.Presenter {

    BusinessCardRepository repository = Injection.BUSINESS_CARD_REPOSITORY;
    File tempFile = null;

    /**
     * 파라미터로 전달받은 정보로 데이터를 가져와 View 의 정보를 변경
     */
    @Override
    public void getChangeEmpData(String empId) {

        getBusinessCardInfo(BusinessCardApplication.getAppContext(), empId, new Callback<BusinessCardModel>() {
            @Override
            public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
                BusinessCardModel model = response.body();

                if (model != null) {
                    BusinessCardEntity entity = new BusinessCardEntity();
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

                    ((BusinessCardActivity) mView).changeBusinessCardView(entity);
                }
            }

            @Override
            public void onFailure(Call<BusinessCardModel> call, Throwable t) {
                Log.d("test", t.toString());
            }
        });
    }

    private void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback) {
        repository.getBusinessCardInfo(context, id, callback);
    }


    /**
     * View에 있는 명함을 Bitmap 으로 받기 위해 사용
     */
    @Override
    public Bitmap getBusinessCardBitmap() {
        if (((BusinessCardActivity) mView).businessCardView == null) {
            return null;
        }

        return getBitmapFromView(((BusinessCardActivity) mView).businessCardView);
    }

    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap;
        View temp = ((AppCompatActivity) mView).findViewById(R.id.businessCard);
        bitmap = Bitmap.createBitmap(temp.getMeasuredWidth(), temp.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 서버에 명함을 image file을 저장
     */
    @Override
    public void saveBusinessCardImage(Context context, String seq, File file, Callback<String> callback) {
        repository.saveBusinessCardImage(context, seq, file, callback);
    }

    /**
     * View의 imageView를 임시 파일 객체에 png 로 저장하여 공유기능으로 보내기
     */
    @Override
    public void sendBusinessCard() {
        tempFile = saveBitmapToPng(getBusinessCardBitmap(), "BusinessCard");
        shareMMS(getUri(tempFile));
    }

    private File saveBitmapToPng(Bitmap bitmap, String name) {
        File resultFile = null;
        try {
            File tempDir = ((AppCompatActivity) mView).getCacheDir();
            resultFile = File.createTempFile("BUSINESS_CARD_PNG_", ".png", tempDir);
            FileOutputStream out = new FileOutputStream(resultFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("test", resultFile.getAbsolutePath() + "이미지 다운로드");
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("test", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("test", "IOException : " + e.getMessage());
        }
        return resultFile;
    }

    private Uri getUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// API 24 이상 일경우..
            uri = FileProvider.getUriForFile(((Context) mView),
                    ((Context) mView).getPackageName() + ".fileprovider", file);
            Log.d("test", ((Context) mView).getPackageName() + ".fileprovider");
        } else {// API 24 미만 일경우..
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    public void shareMMS(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent chooser = Intent.createChooser(intent, "send");
            List<ResolveInfo> resInfoList = ((BusinessCardActivity) mView).getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                ((BusinessCardActivity) mView).grantUriPermission(packageName, intent.getParcelableExtra(Intent.EXTRA_STREAM), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            ((BusinessCardContract.View) mView).sendMMS(chooser);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(((BusinessCardActivity) mView).getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 서버에 명함을 image file 을 저장
     */
    @Override
    public void sendBusinessCard(SendBusinessCardModel model) {

        repository.sendBusinessCard((Context) mView, model, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText((Context) mView, "메시지 요청 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    /**
     * 로컬에 저장되어있는 임시 파일을 삭제
     */
    @Override
    public void deleteTempBusinessCardFile() {
        if (tempFile != null) {
            tempFile.deleteOnExit();
        }
    }

//    /**
//     * 서버 DB에 명함정보가 이미지로 저장되어있는지 확인하는 메소드
//     */
//    @Override
//    public void hasBusinessCard(Context context, String seq) {
//
//        Callback<String> callback = new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.body().equals("false")) {
//                    // 명함 이미지가 없는 경우
//
//                    // 데이터 가져오기
//                    getBusinessCardInfo(BusinessCardApplication.getAppContext(), seq, new Callback<BusinessCardModel>() {
//                        @Override
//                        public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
//                            BusinessCardModel model = response.body();
//
//                            if (model != null) {
////                                entity.setId(model.getSeq());
////                                entity.setName(model.getName());
////                                entity.setAddress(model.getAddress());
////                                entity.setEmail(model.getEmail());
////                                entity.setTel(model.getTel());
////                                entity.setMobile(model.getPhone());
////                                entity.setTeam(model.getTeam());
////                                entity.setPosition(model.getPosition());
////                                entity.setFax(model.getFax());
////                                Log.d("test", entity.toString() + "");
////
////                                data.setValue(entity);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<BusinessCardModel> call, Throwable t) {
//                            Log.d("test", t.toString());
//                        }
//                    });
//
//                } else {
////                    getBusinessCardImage();
//                }
//                // 명함 이미지가 있는 경우
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("test", t.toString());
//            }
//        };
//
//
//        repository.hasBusinessCard(context, seq, callback);
//    }
}
