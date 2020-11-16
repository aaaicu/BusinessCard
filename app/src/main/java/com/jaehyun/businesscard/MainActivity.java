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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaehyun.businesscard.database.BusinessDB;
import com.jaehyun.businesscard.database.entity.BusinessCardEntity;
import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.network.RetrofitFactory;
import com.jaehyun.businesscard.network.repository.EmployeeRepository;
import com.jaehyun.businesscard.network.service.BusinessCardService;
import com.jaehyun.businesscard.util.Config;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText editTextSeq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSeq = findViewById(R.id.editTextEmpSeq);
    }

    public void openEditBusinessCardActivity(View view) {

        Intent intent = new Intent(this,BusinessCardEditActivity.class);
        startActivity(intent);
    }

    public void requestBusinessCard(View view) {
        if(editTextSeq.getText().toString() != null ){
            EmployeeRepository.getInstance().getBusinessCardInfo(editTextSeq.getText().toString()).enqueue(new Callback<BusinessCardModel>() {
                @Override
                public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
                    Toast.makeText(getApplicationContext(), "성공",Toast.LENGTH_SHORT).show();
                    BusinessCardModel model = response.body();

                    if(model == null){
                        Toast.makeText(getApplicationContext(), "해당 사번 없음",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    BusinessCardEntity entity =  new BusinessCardEntity();
                    entity.setId(model.getSeq());
                    entity.setName(model.getName());
                    entity.setAddress(model.getAddress());
                    entity.setEmail(model.getEmail());
                    entity.setTel(model.getTel());
                    entity.setMobile(model.getPhone());
                    entity.setTeam(model.getTeam());
                    entity.setPosition(model.getPosition());
                    entity.setSetFax(model.getFax());

                    BusinessCardApplication.getDatabase().businessCardDao().deleteBusinessCard(entity);
                    BusinessCardApplication.getDatabase().businessCardDao().insertBusinessCard(entity);

                    Intent intent = new Intent(getApplicationContext(),BusinessCardActivity.class);
                    intent.putExtra("REQ","server");
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<BusinessCardModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "요청실패",Toast.LENGTH_SHORT).show();
                    Log.d("test","실패");
                    Log.d("test",t.toString());
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "사번을 입력하세요",Toast.LENGTH_SHORT).show();
        }
    }
}
