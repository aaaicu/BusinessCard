package com.jaehyun.businesscard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaehyun.businesscard.database.entity.BusinessCardEntity;
import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.network.repository.EmployeeRepository;
import com.jaehyun.businesscard.view.BusinessCardView;
import com.jaehyun.businesscard.viewmodel.BusinessCardViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCardActivity extends AppCompatActivity {
    BusinessCardViewModel businessCardViewModel;
    BusinessCardView businessCardView = null;
    ImageView imageView = null;
    MutableLiveData<BusinessCardEntity> data = null;
    Bitmap bitmap =null;
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
        checkPermission();

        businessCardView = findViewById(R.id.businessCardView);
        imageView = findViewById(R.id.imageView);

        businessCardViewModel = new BusinessCardViewModel(getApplication());
        businessCardViewModel.setEntity(new MutableLiveData<>());
        data = businessCardViewModel.getEntity();

        data.observe(this, businessCardEntity ->{
            businessCardView.setBusinessCardData(businessCardEntity);
            Bitmap bitmap = getBitmapFromView(businessCardView);
            imageView.setImageBitmap(bitmap);

            // 서버로 이미지 전달
            if (getIntent().getStringExtra("REQ") != null ){
                if (getIntent().getStringExtra("REQ").equals("server") ){
                    Log.d("test", "서버로 이미지 저장 요청" );
                    Log.d("test", getIntent().getIntExtra("ID",-1) + "" );
                    File temp = saveBitmapToPng(bitmap,"BusinessCard");

                    EmployeeRepository.getInstance()
                            .saveBusinessCardImage(getIntent().getIntExtra("ID",-1),temp)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    temp.delete();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    temp.delete();
                                }
                            });
                }
            }
        });
        observeRoomDB(getIntent().getIntExtra("ID",-1));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMG_SEND){
            tempFile.deleteOnExit();
        }
    }

    private void observeRoomDB(int empId){
        BusinessCardApplication.getDatabase()
                .businessCardDao()
                .getBusinessCard(empId)
                .observe(this, businessCardEntity -> {
                    if (businessCardEntity != null){
                        Log.d("test", "관찰 테스트" + businessCardEntity.toString());
                        data.setValue(businessCardEntity);
                    }
        });
    }

    public Bitmap getBitmapFromView(View v){
        if(bitmap == null){
            View temp = findViewById(R.id.businessCard);
            bitmap = Bitmap.createBitmap(temp.getMeasuredWidth(),temp.getMeasuredHeight() , Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private File saveBitmapToPng(Bitmap bitmap, String name){

        try {
            File tempDir = getCacheDir();
            tempFile = File.createTempFile("BUSINESS_CARD_PNG_",".png",tempDir);
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("test",tempFile.getAbsolutePath()+"이미지 다운로드");
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("test","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("test","IOException : " + e.getMessage());
        }
        return tempFile;
    }

    public void sendBusinessCard(View view) {

        saveBitmapToPng(bitmap,"BusinessCard");
        sendMMS(getUri(tempFile));
    }

    private void sendMMS(Uri uri) {

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, "send"), REQUEST_IMG_SEND);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getUri(File file){
        Uri uri = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {// API 24 이상 일경우..
            uri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".fileprovider", file);
        }
        else
        {// API 24 미만 일경우..
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }
}
