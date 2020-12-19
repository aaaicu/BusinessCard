package com.jaehyun.businesscard.ui.businesscard;

import android.content.Context;
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
    }

    interface Presenter {
        void hasBusinessCard(Context context, String seq, Callback<String> callback);

        void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback);

        void saveBusinessCardImage(Context context, String id, File file, Callback<String> callback);

        void sendBusinessCard(SendBusinessCardModel model, Callback<Void> callback);

        Bitmap getBitmapFromView(android.view.View v);

        File saveBitmapToPng(Bitmap bitmap, String name);

        void sendBusinessCard(File tempFile);

        void sendMMS(Uri uri);

        public Uri getUri(File file);
    }
}
