package com.jaehyun.businesscard.ui.base;

import android.content.Intent;

public interface BaseContract {
    interface View {
        void showToast(String msg);
        void openActivity(Intent intent);
    }

    interface Presenter{
        void setView(BaseContract.View view);
    }
}
