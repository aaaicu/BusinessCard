package com.jaehyun.businesscard.ui.businesscard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;

import java.io.File;

import retrofit2.Callback;

public interface BusinessCardContract {
    interface View {
        void checkPermission();

        void sendBusinessCard(android.view.View view);

        void sendMMS(Intent intent);
    }

    interface Presenter {
        void hasBusinessCard(Context context, String seq, Callback<String> callback);

        void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback);

        void saveBusinessCardImage(Context context, String id, File file, Callback<String> callback);

        void sendBusinessCard(SendBusinessCardModel model, Callback<Void> callback);

        Bitmap getBitmapFromView(android.view.View v);

        File saveBitmapToPng(Bitmap bitmap, String name);

        void sendBusinessCard();

        void sendMMS(Uri uri);

        Uri getUri(File file);

        void deleteTempFile();

    }
}
