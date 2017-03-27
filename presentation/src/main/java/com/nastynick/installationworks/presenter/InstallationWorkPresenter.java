package com.nastynick.installationworks.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.nastynick.installationworks.PostExecutionThread;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.UploadFileUseCase;
import com.nastynick.installationworks.mapper.InstallationWorkCapture;
import com.nastynick.installationworks.mapper.InstallationWorkQrCodeMapper;
import com.nastynick.installationworks.util.InstallationFileCreator;
import com.nastynick.installationworks.util.WaterMarker;
import com.nastynick.installationworks.view.InstallationWorkCaptureView;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class InstallationWorkPresenter {
    @Inject
    protected PostExecutionThread postExecutionThread;
    @Inject
    protected UploadFileUseCase uploadFileUseCase;
    @Inject
    protected InstallationWorkCapture installationWorkCapture;

    private InstallationWorkQrCodeMapper mapper;
    private InstallationWorkCaptureView installationWorkCaptureView;
    private Context context;

    @Inject
    public InstallationWorkPresenter(InstallationWorkQrCodeMapper mapper, Context context) {
        this.mapper = mapper;
        this.context = context;
    }

    public void setInstallationWorkCaptureView(InstallationWorkCaptureView installationWorkCaptureView) {
        this.installationWorkCaptureView = installationWorkCaptureView;
    }

    public void transformCodeToInstallationWork(String code) {
        installationWorkCapture.setInstallationWork(mapper.transform(code));
    }

    public void installationWorkCaptured() {
        installationWorkCaptureView.showLoadingView();
        Observable.just(installationWorkCapture.getFile())
                .subscribeOn(Schedulers.io())
                .doOnNext(file -> WaterMarker.resizeImage(file, 1024, getWaterMarkTitle(), new ImageObserver()))
                .subscribe();
    }

    private String getWaterMarkTitle() {
        return String.format(context.getResources().getString(R.string.installation_work_photo_water_mark),
                installationWorkCapture.getInstallationWork().getConstructionNumber(), installationWorkCapture.getInstallationWork().getAddress());
    }

    public Uri createFile() {
        String[] directories = mapper.getInstallationWorkDirectories(installationWorkCapture.getInstallationWork());
        File file = InstallationFileCreator.createFile(installationWorkCapture.getInstallationWork().getConstructionNumber(),
                context.getResources().getString(R.string.installation_work_root), directories);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

        installationWorkCapture.setFile(file);
        installationWorkCapture.setUri(uri);
        return uri;
    }

    private class ImageObserver extends DisposableObserver {
        @Override
        public void onNext(Object value) {
        }

        @Override
        public void onError(Throwable e) {
            Observable.just(installationWorkCaptureView)
                    .observeOn(postExecutionThread.getScheduler())
                    .subscribe(t -> {
                        installationWorkCaptureView.hideLoadingView();
                        installationWorkCaptureView.imageFailed();
                    });
        }

        @Override
        public void onComplete() {
            Observable.just(installationWorkCaptureView)
                    .subscribeOn(Schedulers.io())
                    .observeOn(postExecutionThread.getScheduler())
                    .doOnNext(t -> {
                        installationWorkCaptureView.hideLoadingView();
                        installationWorkCaptureView.imageSuccess();
                    })
                    .subscribe(t -> uploadFileUseCase.uploadFile(new InstallationFileUploadObservable(), installationWorkCapture.getFile()));
        }
    }

    private class InstallationFileUploadObservable extends DisposableObserver<ResponseBody> {

        @Override
        public void onNext(ResponseBody responseBody) {
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
            }
        }

        @Override
        public void onComplete() {
            installationWorkCaptureView.hideLoadingView();
        }
    }
}
