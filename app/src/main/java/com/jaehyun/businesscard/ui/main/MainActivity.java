package com.jaehyun.businesscard.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jaehyun.businesscard.ui.edit.BusinessCardEditActivity;
import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.ui.webview.WebViewActivity;
import com.jaehyun.businesscard.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainContract.View {
    EditText editTextId = null;
    MainPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter();
        presenter.setView(this);

        setContentView(R.layout.activity_main);
        editTextId = findViewById(R.id.editTextEmpId);
    }

    public void openEditBusinessCardActivity(View view) {
        Intent intent = new Intent(this, BusinessCardEditActivity.class);
        openActivity(intent);
    }

    @Override
    public void clickRequestBusinessCard(View view) {
        presenter.requestBusinessCard(editTextId.getText().toString());
    }

    @Override
    public void clickWebViewActivity(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void clickSessionTest(View view) {
        presenter.requestSession(this);
    }


}
