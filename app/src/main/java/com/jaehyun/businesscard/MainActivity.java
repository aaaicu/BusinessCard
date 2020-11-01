package com.jaehyun.businesscard;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jaehyun.businesscard.database.BusinessDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

        public Bitmap getBitmapFromView(View v){
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight() , Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
         v.draw(c);
         return b;
    }

    public void openEditBusinessCardActivity(View view) {

        Intent intent = new Intent(this,BusinessCardEditActivity.class);
        startActivity(intent);
    }
}
