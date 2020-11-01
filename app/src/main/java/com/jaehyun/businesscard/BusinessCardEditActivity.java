package com.jaehyun.businesscard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jaehyun.businesscard.database.BusinessDB;
import com.jaehyun.businesscard.database.entity.BusinessCardEntity;

public class BusinessCardEditActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextTel;
    private EditText editTextMobile;
    private EditText editTextTeam;
    private EditText editTextPosition;
    private EditText editTextAddress;

    Thread databaseThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card_edit);
        editTextName = findViewById(R.id.editName);
        editTextEmail = findViewById(R.id.editEmail);
        editTextTel = findViewById(R.id.editTel);
        editTextMobile = findViewById(R.id.editMobile);
        editTextTeam = findViewById(R.id.editTeam);
        editTextPosition = findViewById(R.id.editPosition);
        editTextAddress = findViewById(R.id.editAddress);

    }

    public void getBusinessCardActivity(View view) {

        BusinessCardEntity entity =  new BusinessCardEntity();
        entity.setName(editTextName.getText().toString());
        entity.setAddress(editTextAddress.getText().toString());
        entity.setEmail(editTextEmail.getText().toString());
        entity.setTel(editTextTel.getText().toString());
        entity.setMobile(editTextMobile.getText().toString());
        entity.setTeam(editTextTeam.getText().toString());
        entity.setPosition(editTextPosition.getText().toString());

        databaseThread = new Thread(() -> BusinessCardApplication.getDatabase().businessCardDao().insertBusinessCard(entity));
        databaseThread.start();

        Intent intent = new Intent(this,BusinessCardActivity.class);
        startActivity(intent);
    }
}
