package com.nastynick.installationworks.view.activty;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivityInstallationWorkCaptureBinding;
import com.nastynick.installationworks.presenter.InstallationWorkPresenter;
import com.nastynick.installationworks.util.PermissionChecker;
import com.nastynick.installationworks.view.InstallationWorkCaptureView;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class InstallationWorkCaptureActivity extends BaseActivity implements InstallationWorkCaptureView {
    public static final int REQUEST_QR_CODE_READ = 100;
    public static final int REQUEST_IMAGE_CAPTURE = 200;
    public static final int PERMISSION_CAMERA = 300;
    public static final int PERMISSION_EXTERNAL_STORAGE = 400;

    public static final String QR_CODE = "qrcode";

    @Inject
    protected InstallationWorkPresenter installationWorkPresenter;
    private ActivityInstallationWorkCaptureBinding binding;
    private String qrCode;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_installation_work_capture);
        getAppComponent().inject(this);
        installationWorkPresenter.setInstallationWorkCaptureView(this);
        PermissionChecker.checkPermission(this, Manifest.permission.CAMERA, PERMISSION_CAMERA);
    }

    public void onScanClick(View view) {
        scanOrTakePhoto();
    }

    public void onReScanClick(View view) {
        dispatchQrCodeScannerIntent();
    }

    public void scanOrTakePhoto() {
        if (qrCode == null) {
            dispatchQrCodeScannerIntent();
        } else checkPermissionAndTakePicture();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = installationWorkPresenter.createFile();
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchQrCodeScannerIntent() {
        startActivityForResult(new Intent(this, QrReaderActivity.class), REQUEST_QR_CODE_READ);
    }

    private void processQrCode() {
        toast(qrCode);
        binding.rescan.setVisibility(View.VISIBLE);
        binding.scan.setText(R.string.qrcode_take_photo);
        installationWorkPresenter.transformCodeToInstallationWork(qrCode);
    }

    @Override
    public void viewPhoto(Bitmap photo) {
        Observable.just(photo)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> binding.photo.setImageBitmap(bitmap));
//        binding.photo.setImageBitmap(photo);
    }

    @Override
    public void showLoadingView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.installation_work_photo_processing_title);
        progressDialog.setMessage(getResources().getString(R.string.installation_work_photo_processing_message));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public void hideLoadingView() {
        progressDialog.hide();
    }

    @Override
    public void imageSuccess() {
        toast(R.string.installation_work_photo_uploaded);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (REQUEST_QR_CODE_READ == requestCode) {
                this.qrCode = data.getStringExtra(QR_CODE);
                processQrCode();
            } else if (REQUEST_IMAGE_CAPTURE == requestCode) {
                installationWorkPresenter.installationWorkCaptured();
            }
        }
    }

    private void checkPermissionAndTakePicture() {
        if (PermissionChecker.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_EXTERNAL_STORAGE)) {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                break;
            }
        }
    }
}
