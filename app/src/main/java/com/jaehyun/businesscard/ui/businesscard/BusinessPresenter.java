package com.jaehyun.businesscard.ui.businesscard;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.jaehyun.businesscard.BusinessCardApplication;
import com.jaehyun.businesscard.R;
import com.jaehyun.businesscard.data.model.SendBusinessCardModel;
import com.jaehyun.businesscard.data.repository.BusinessCardRepository;
import com.jaehyun.businesscard.data.model.BusinessCardModel;
import com.jaehyun.businesscard.ui.base.BasePresenter;
import com.jaehyun.businesscard.util.Injection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Callback;

public class BusinessPresenter extends BasePresenter implements BusinessCardContract.Presenter {

    BusinessCardRepository repository = Injection.BUSINESS_CARD_REPOSITORY;
    File tempFile = null;

    @Override
    public void hasBusinessCard(Context context, String seq, Callback<String> callback) {
        repository.hasBusinessCard(context, seq, callback);
    }

    @Override
    public void getBusinessCardInfo(Context context, String id, Callback<BusinessCardModel> callback) {
        repository.getBusinessCardInfo(context, id, callback);
    }

    @Override
    public void saveBusinessCardImage(Context context, String seq, File file, Callback<String> callback) {
        repository.saveBusinessCardImage(context, seq, file, callback);
    }

    @Override
    public void sendBusinessCard(SendBusinessCardModel model, Callback<Void> callback) {
        repository.sendBusinessCard((Context) mView, model, callback);
    }

    @Override
    public Bitmap getBitmapFromView(View v) {
        Bitmap bitmap;
        View temp = ((AppCompatActivity) mView).findViewById(R.id.businessCard);
        bitmap = Bitmap.createBitmap(temp.getMeasuredWidth(), temp.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    @Override
    public File saveBitmapToPng(Bitmap bitmap, String name) {
        File resultFile = null;
        try {
            File tempDir = ((AppCompatActivity) mView).getCacheDir();
            resultFile = File.createTempFile("BUSINESS_CARD_PNG_", ".png", tempDir);
            FileOutputStream out = new FileOutputStream(resultFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("test", resultFile.getAbsolutePath() + "이미지 다운로드");
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("test", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("test", "IOException : " + e.getMessage());
        }
        return resultFile;
    }

    @Override
    public void sendBusinessCard() {
        ImageView imageView = ((BusinessCardActivity) mView).imageView;
        if (imageView != null) {
            tempFile = saveBitmapToPng(((BitmapDrawable) imageView.getDrawable()).getBitmap(), "BusinessCard");
            sendMMS(getUri(tempFile));
        }
    }

    @Override
    public void sendMMS(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            //TODO:view 에서처리하도록 후처리

            Intent chooser = Intent.createChooser(intent, "send");
            List<ResolveInfo> resInfoList = ((BusinessCardActivity) mView).getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                ((BusinessCardActivity) mView).grantUriPermission(packageName, intent.getParcelableExtra(Intent.EXTRA_STREAM), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            ((BusinessCardContract.View) mView).sendMMS(chooser);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(((BusinessCardActivity) mView).getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Uri getUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// API 24 이상 일경우..
            uri = FileProvider.getUriForFile(((Context) mView),
                    ((Context) mView).getPackageName() + ".fileprovider", file);
            Log.d("test", ((Context) mView).getPackageName() + ".fileprovider");
        } else {// API 24 미만 일경우..
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    public void deleteTempFile() {
        if (tempFile != null) {
            tempFile.deleteOnExit();
        }
    }

}
