package com.jaehyun.businesscard;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jaehyun.businesscard.database.entity.BusinessCardEntity;
import com.jaehyun.businesscard.model.BusinessCardModel;
import com.jaehyun.businesscard.network.repository.EmployeeRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText editTextId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextId = findViewById(R.id.editTextEmpId);
    }

    public void openEditBusinessCardActivity(View view) {

        Intent intent = new Intent(this,BusinessCardEditActivity.class);
        startActivity(intent);
    }

    public void requestBusinessCard(View view) {
        if(editTextId.getText().toString() != null ){

            EmployeeRepository.getInstance().getBusinessCardInfo(this, editTextId.getText().toString()).enqueue(new Callback<BusinessCardModel>(){

                @Override
                public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
                    Toast.makeText(getApplicationContext(), "사원정보가 조회되었습니다.",Toast.LENGTH_SHORT).show();
                    BusinessCardModel model = response.body();
                    if(model == null){
                        Toast.makeText(getApplicationContext(), "해당 사번 없음",Toast.LENGTH_SHORT).show();

                    }else {
                        Intent intent = new Intent(getApplicationContext(),BusinessCardActivity.class);
                        intent.putExtra("REQ","server");
                        intent.putExtra("ID",editTextId.getText().toString());

                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<BusinessCardModel> call, Throwable t) {
                    Log.d("test", t.toString());
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "사번을 입력하세요",Toast.LENGTH_SHORT).show();
        }
    }
    public void openWebViewActivity(View view) {

        Intent intent = new Intent(this,WebViewActivity.class);
        startActivity(intent);
    }

}
