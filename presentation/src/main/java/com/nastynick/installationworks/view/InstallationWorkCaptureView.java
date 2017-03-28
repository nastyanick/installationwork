package com.nastynick.installationworks.view;

public interface InstallationWorkCaptureView {

    void showLoadingView(boolean upload);

    void hideLoadingView();

    void imageSuccess(int message);

    void imageFailed();

    void qrCodeFailed();

    void setProgress(Integer progress);

    void onFinish();
}
