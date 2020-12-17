package com.jaehyun.businesscard.ui.businesscard;

import android.content.Context;

import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;

import java.io.File;

import retrofit2.Callback;

public interface BusinessCardContract {
    interface View {
    }

    interface Presenter {
        void hasBusinessCard(Context context, String seq, Callback<String> callback);

        void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback);

        void saveBusinessCardImage(Context context, String id, File file, Callback<String> callback);

        void sendBusinessCard(Context context, SendBusinessCardModel model, Callback<Void> callback);
    }
}
