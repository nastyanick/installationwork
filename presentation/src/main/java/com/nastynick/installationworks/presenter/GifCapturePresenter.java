package com.nastynick.installationworks.presenter;

import android.content.Context;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.GifCreating;
import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.view.camera.CameraBurstView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GifCapturePresenter {
    @Inject
    SettingsUseCase settingsUseCase;
    @Inject
    GifCreating gifCreating;
    @Inject
    Context context;
    private CameraBurstView view;
    private List<String> images = new ArrayList<>();

    @Inject
    public GifCapturePresenter() {
    }

    public void setGifCameraView(CameraBurstView view) {
        this.view = view;
    }

    public void onStart() {
        int imagesCount = settingsUseCase.getGifFramesCount();
        view.setTotalImagesCount(imagesCount);
        updateTakenImagesCount();
    }

    public void updateTakenImagesCount() {
        view.setTakenFramesCount(images.size());
    }

    public void addImage(byte[] picture) {
        Single.just(picture)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(pic -> gifCreating.createPictureFile(picture, context.getString(R.string.installation_work_root)))
                .subscribe(this::onImageSaved);

    }

    public void onImageSaved(String file) {
        images.add(file);
        updateTakenImagesCount();

        if (images.size() >= settingsUseCase.getGifFramesCount()) {
            view.finishImagesTaking(images);
        }
    }
}
