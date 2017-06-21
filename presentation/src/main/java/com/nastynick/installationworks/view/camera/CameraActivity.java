package com.nastynick.installationworks.view.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.nastynick.installationworks.R;


public class CameraActivity extends Activity {
    private CameraView cameraView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = (CameraView) findViewById(R.id.camera);
        View capture = findViewById(R.id.button_capture);

        capture.setOnClickListener((v) -> cameraView.captureImage());
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            }
        });
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
}