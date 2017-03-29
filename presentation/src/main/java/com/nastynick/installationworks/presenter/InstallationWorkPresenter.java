package com.nastynick.installationworks.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.nastynick.installationworks.InstallationWork;
import com.nastynick.installationworks.InstallationWorkCapture;
import com.nastynick.installationworks.PostExecutionThread;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.interactor.UploadFileUseCase;
import com.nastynick.installationworks.mapper.InstallationWorkDataMapper;
import com.nastynick.installationworks.mapper.InstallationWorkQrCodeMapper;
import com.nastynick.installationworks.util.WaterMarker;
import com.nastynick.installationworks.view.InstallationWorkCaptureView;

import java.io.File;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class InstallationWorkPresenter {
    protected InstallationWorkCapture installationWorkCapture;
    private InstallationWorkDataMapper dataMapper;
    private InstallationWorkQrCodeMapper mapper;
    private PostExecutionThread postExecutionThread;
    private UploadFileUseCase uploadFileUseCase;
    private SettingsUseCase settings;
    private Context context;
    private InstallationWorkCaptureView installationWorkCaptureView;

    @Inject
    public InstallationWorkPresenter(InstallationWorkQrCodeMapper mapper, SettingsUseCase settings, Context context,
                                     PostExecutionThread postExecutionThread, UploadFileUseCase uploadFileUseCase,
                                     InstallationWorkCapture installationWorkCapture, InstallationWorkDataMapper dataMapper) {
        this.mapper = mapper;
        this.settings = settings;
        this.context = context;
        this.postExecutionThread = postExecutionThread;
        this.uploadFileUseCase = uploadFileUseCase;
        this.installationWorkCapture = installationWorkCapture;
        this.dataMapper = dataMapper;
    }

    public void setInstallationWorkCaptureView(InstallationWorkCaptureView installationWorkCaptureView) {
        this.installationWorkCaptureView = installationWorkCaptureView;
    }

    public void transformCodeToInstallationWork(String code) {
        InstallationWork transform = mapper.transform(code);
        if (transform == null) {
            installationWorkCaptureView.qrCodeFailed();
            installationWorkCaptureView.onFinish();
        } else installationWorkCapture.setInstallationWork(transform);
    }

    public void installationWorkCaptured() {
        installationWorkCaptureView.showLoadingView(false);
        Observable.just(installationWorkCapture.getFile())
                .subscribeOn(Schedulers.io())
                .doOnNext(file -> WaterMarker.resizeImage(file, settings.getWidth(),
                        installationWorkCapture.getInstallationWork().getTitle(), new ImageObserver()))
                .subscribe();
    }

    public Uri createFile() {
        String fileName = installationWorkCapture.getInstallationWork().getTitle();
        String root = context.getResources().getString(R.string.installation_work_root);
        String[] directories = mapper.getInstallationWorkDirectories(root, installationWorkCapture.getInstallationWork());
        File file = uploadFileUseCase.createFile(directories, fileName);
        installationWorkCapture.setFile(file);
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
    }

    private void uploadImage() {
        String root = context.getResources().getString(R.string.installation_work_root);
        Observable.just(installationWorkCaptureView)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler())
                .doOnNext(t -> {
                    installationWorkCaptureView.hideLoadingView();
                    installationWorkCaptureView.imageSuccess(R.string.installation_work_photo_saved);
                    installationWorkCaptureView.showLoadingView(true);
                })
                .subscribe(t -> {
                    InstallationWork installationWork = installationWorkCapture.getInstallationWork();
                    uploadFileUseCase.uploadFile(new FileUploadObservable(installationWork),
                            new ProgressObserver(installationWork), mapper.getInstallationWorkDirectories(root, installationWork),
                            installationWorkCapture.getFile());
                });
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
            uploadImage();
        }
    }

    private class ProgressObserver extends DisposableObserver<Integer> {
        InstallationWork installationWork;

        public ProgressObserver(InstallationWork installationWork) {
            this.installationWork = installationWork;
        }

        @Override
        public void onNext(Integer value) {
            installationWorkCaptureView.setProgress(value);
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException) {
                saveInstallationWork(installationWork);
            }
            installationWorkCaptureView.hideLoadingView();
            installationWorkCaptureView.imageFailed();
        }

        @Override
        public void onComplete() {
        }
    }

    private void saveInstallationWork(InstallationWork installationWork) {
        dataMapper.saveToData(installationWork);
    }

    private class FileUploadObservable extends DisposableObserver<ResponseBody> {
        InstallationWork installationWork;

        public FileUploadObservable(InstallationWork installationWork) {
            this.installationWork = installationWork;
        }

        @Override
        public void onNext(ResponseBody responseBody) {
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException) {
                saveInstallationWork(installationWork);
            }
        }

        @Override
        public void onComplete() {
            installationWorkCapture.clear();
            installationWorkCaptureView.imageSuccess(R.string.installation_work_photo_uploaded);
            installationWorkCaptureView.hideLoadingView();
            installationWorkCaptureView.onFinish();
        }
    }
}
