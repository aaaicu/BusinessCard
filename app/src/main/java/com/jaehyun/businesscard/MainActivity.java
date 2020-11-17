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

            EmployeeRepository.getInstance().getBusinessCardInfo(editTextId.getText().toString()).enqueue(new Callback<BusinessCardModel>(){

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

                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "사번을 입력하세요",Toast.LENGTH_SHORT).show();
        }
//            EmployeeRepository.getInstance().hasBusinessCard(editTextId.getText().toString()).enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
////                    Log.d("test","성공" + response.body());
//                    if (response.body().equals("true")) {
//
//                        Intent intent = new Intent(getApplicationContext(),BusinessCardActivity.class);
//                        intent.putExtra("REQ","server");
//                        intent.putExtra("ID",editTextId.getText().toString());
//
//                        startActivity(intent);
//                    } else{
//                        Toast.makeText(getApplicationContext(), "해당 사번 없음",Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
////                    Log.d("test",t.toString());
//                }
//            });


//            EmployeeRepository.getInstance().getBusinessCardInfo(editTextId.getText().toString()).enqueue(new Callback<BusinessCardModel>() {
//                @Override
//                public void onResponse(Call<BusinessCardModel> call, Response<BusinessCardModel> response) {
//                    Toast.makeText(getApplicationContext(), "사원정보가 조회되었습니다.",Toast.LENGTH_SHORT).show();
//                    BusinessCardModel model = response.body();
//
//                    if(model == null){
//                        Toast.makeText(getApplicationContext(), "해당 사번 없음",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    BusinessCardEntity entity =  new BusinessCardEntity();
//                    entity.setId(model.getSeq());
//                    entity.setName(model.getName());
//                    entity.setAddress(model.getAddress());
//                    entity.setEmail(model.getEmail());
//                    entity.setTel(model.getTel());
//                    entity.setMobile(model.getPhone());
//                    entity.setTeam(model.getTeam());
//                    entity.setPosition(model.getPosition());
//                    entity.setSetFax(model.getFax());
//
//                    BusinessCardApplication.getDatabase().businessCardDao().deleteBusinessCard(entity);
//                    BusinessCardApplication.getDatabase().businessCardDao().insertBusinessCard(entity);
//
//                    Intent intent = new Intent(getApplicationContext(),BusinessCardActivity.class);
//
//                    Log.d("test",entity.toString()+"");
//                    intent.putExtra("REQ","server");
//                    intent.putExtra("ID",model.getSeq());
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onFailure(Call<BusinessCardModel> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "요청실패",Toast.LENGTH_SHORT).show();
//                    Log.d("test","실패");
//                    Log.d("test",t.toString());
//                }
//            });


    }
}
