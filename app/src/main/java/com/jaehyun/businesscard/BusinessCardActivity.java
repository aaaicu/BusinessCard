package com.jaehyun.businesscard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaehyun.businesscard.database.entity.BusinessCardEntity;
import com.jaehyun.businesscard.view.BusinessCardView;
import com.jaehyun.businesscard.viewmodel.BusinessCardViewModel;

public class BusinessCardActivity extends AppCompatActivity {
    BusinessCardViewModel businessCardViewModel;
    BusinessCardView businessCardView = null;
    ImageView imageView = null;
    MutableLiveData<BusinessCardEntity> data = null;
    Bitmap bitmap =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        businessCardView = findViewById(R.id.businessCardView);
        imageView = findViewById(R.id.imageView);

        businessCardViewModel = new BusinessCardViewModel(getApplication());
        businessCardViewModel.setEntity(new MutableLiveData<>());
        data = businessCardViewModel.getEntity();

        BusinessCardApplication.getDatabase().businessCardDao().getBusinessCard(1).observe(this, businessCardEntity -> {
            if (businessCardEntity != null){
                Log.d("test", "관찰 테스트" + businessCardEntity.toString());
                data.setValue(businessCardEntity);
            }
        });

        data.observe(this, businessCardEntity ->{
            businessCardView.setBusinessCardData(businessCardEntity);
            Bitmap bitmap = getBitmapFromView(businessCardView);
            imageView.setImageBitmap(bitmap);
         });
        }

    public Bitmap getBitmapFromView(View v){
        //findViewById(R.id.businessCard).getMeasuredWidth() == 2100
        //findViewById(R.id.businessCard).getMeasuredHeight() == 1200
        if(bitmap == null){
            bitmap = Bitmap.createBitmap(2100,1200 , Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public void sendBusinessCard(View view) {
    }
//    private void sendMMS(Uri uri) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("image/*");
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivityForResult(Intent.createChooser(intent, "send"), REQUEST_IMG_SEND);
//        }
//        catch (ActivityNotFoundException e) {
//            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
//        }
//    }

}
