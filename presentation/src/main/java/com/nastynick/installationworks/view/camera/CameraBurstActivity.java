package com.nastynick.installationworks.view.camera;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.presenter.GifCapturePresenter;
import com.nastynick.installationworks.view.activty.BaseActivity;
import com.nastynick.installationworks.view.activty.InstallationWorkCaptureActivity;

import java.util.List;

import javax.inject.Inject;


public class CameraBurstActivity extends BaseActivity implements CameraBurstView {

    @Inject
    GifCapturePresenter presenter;

    private CameraView cameraView;
    private TextView framesCountView;
    private View captureButton;

    private int framesCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initViews();
        initListeners();
        getAppComponent().inject(this);

        presenter.setGifCameraView(this);
    }

    public void initViews() {
        cameraView = (CameraView) findViewById(R.id.camera);
        captureButton = findViewById(R.id.button_capture);
        framesCountView = (TextView) findViewById(R.id.photos_count);
    }

    public void initListeners() {
        captureButton.setOnClickListener((v) -> {
            cameraView.captureImage();
            beep();
        });

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                presenter.addImage(picture);
            }
        });
    }

    private void beep() {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 70);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void setTotalImagesCount(int framesCount) {
        this.framesCount = framesCount;
    }

    @Override
    public void setTakenFramesCount(int framesTaken) {
        framesCountView.setText(String.format(getString(R.string.gif_camera_from), framesTaken, framesCount));
    }

    @Override
    public void finishImagesTaking(List<String> frames) {
        Intent intent = new Intent();
        intent.putExtra(InstallationWorkCaptureActivity.EXTRA_PHOTOS_BURST, frames.toArray(new String[frames.size()]));
        setResult(RESULT_OK, intent);
        finish();
    }
}