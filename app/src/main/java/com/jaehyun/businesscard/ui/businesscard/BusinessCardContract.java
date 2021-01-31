package com.jaehyun.businesscard.ui.businesscard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.jaehyun.businesscard.data.local.BusinessCardEntity;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;

import java.io.File;

import retrofit2.Callback;

public interface BusinessCardContract {
    interface View {

        void changeBusinessCardView(BusinessCardEntity entity);

        void sendBusinessCard(android.view.View view);

        void sendMMS(Intent intent);

        void checkPermission();
    }

    interface Presenter {
        void getChangeEmpData(String empId);

        Bitmap getBusinessCardBitmap();

        void saveBusinessCardImage(Context context, String id, File file, Callback<String> callback);

        void sendBusinessCard(SendBusinessCardModel model);

        void sendBusinessCard();

        void shareMMS(Uri uri);

        void deleteTempBusinessCardFile();

    }
}
