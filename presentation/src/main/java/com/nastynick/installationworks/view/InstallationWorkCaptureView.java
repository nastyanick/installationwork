package com.nastynick.installationworks.view;

import android.graphics.Bitmap;

public interface InstallationWorkCaptureView {
    void viewPhoto(Bitmap photo);

    void showLoadingView();

    void hideLoadingView();

    void imageSuccess();
}
