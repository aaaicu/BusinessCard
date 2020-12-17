package com.jaehyun.businesscard.ui.main;

import android.content.Context;

public interface MainContract {
    interface View {
        void clickRequestBusinessCard(android.view.View view);

        void clickWebViewActivity(android.view.View view);

        void clickSessionTest(android.view.View view);
    }

    interface Presenter {
        void requestBusinessCard(String id);

        void requestSession(Context context);
    }
}
