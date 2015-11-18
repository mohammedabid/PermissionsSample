package com.example.mohammadabidhussain.permissionssample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohammadabid.hussain on 11/16/2015.
 */
public class MyActivity extends MainActivity implements OnPermissionRequestListener {

    private Button btnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initViews();
    }

    private void initViews() {
        btnCamera = (Button) findViewById(R.id.btn_openCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkForPermission(MyActivity.this, Manifest.permission.CAMERA)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = getOutputImageFileUri(MyActivity.this);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 2);
                    } else {
                        requestForPermission(Manifest.permission.CAMERA, 0, MyActivity.this);
                    }
                }
                else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri imageUri = getOutputImageFileUri(MyActivity.this);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    public Uri getOutputImageFileUri(Context ctx) {
        // TODO: check the presence of SDCard

        String tstamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                .format(new Date());
        File file = new File(getTempDirectoryPath(ctx), "IMG_" + tstamp
                + ".jpg");

        return Uri.fromFile(file);

    }

    private String getTempDirectoryPath(Context ctx) {
        File cache = null;

        // SD Card Mounted

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    cache = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath()
                            + "/Android/data/"
                            + ctx.getPackageName() + "/cache/");
                }
                // Use internal storage
                else {
                    cache = ctx.getCacheDir();
                }
            } else {
                requestForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0, this);
            }
        }
        // Create the cache directory if it doesn't exist
        if (cache != null) {
            if (!cache.exists()) {
                cache.mkdirs();
            }

            return cache.getAbsolutePath();
        } else {
            return null;
        }
    }

    @Override
    public void onPermissionsGrantedResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onPermissionsDeniedResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onPermissionsCancelledResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
