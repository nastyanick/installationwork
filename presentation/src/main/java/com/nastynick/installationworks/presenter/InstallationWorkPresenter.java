package com.nastynick.installationworks.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.nastynick.installationworks.InstallationWorkCapture;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.di.app.ConnectionTracker;
import com.nastynick.installationworks.di.app.ExceptionLogManager;
import com.nastynick.installationworks.entity.InstallationWork;
import com.nastynick.installationworks.executor.PostExecutionThread;
import com.nastynick.installationworks.file.FileManager;
import com.nastynick.installationworks.interactor.AbsObserver;
import com.nastynick.installationworks.interactor.ProcessFileUseCase;
import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.mapper.InstallationWorkQrCodeMapper;
import com.nastynick.installationworks.util.MemoryUtil;
import com.nastynick.installationworks.util.WaterMarker;
import com.nastynick.installationworks.view.InstallationWorkCaptureView;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class InstallationWorkPresenter {
    protected InstallationWorkCapture installationWorkCapture;
    private ConnectionTracker connectionTracker;
    private InstallationWorkQrCodeMapper mapper;
    private PostExecutionThread postExecutionThread;
    private ProcessFileUseCase processFileUseCase;
    private SettingsUseCase settings;
    private Context context;
    private InstallationWorkCaptureView installationWorkCaptureView;
    private ExceptionLogManager exceptionLogManager;

    @Inject
    public InstallationWorkPresenter(InstallationWorkQrCodeMapper mapper, SettingsUseCase settings, Context context,
                                     PostExecutionThread postExecutionThread, ProcessFileUseCase processFileUseCase,
                                     InstallationWorkCapture installationWorkCapture, ConnectionTracker connectionTracker,
                                     ExceptionLogManager exceptionLogManager) {
        this.mapper = mapper;
        this.settings = settings;
        this.context = context;
        this.postExecutionThread = postExecutionThread;
        this.processFileUseCase = processFileUseCase;
        this.installationWorkCapture = installationWorkCapture;
        this.connectionTracker = connectionTracker;
        this.exceptionLogManager = exceptionLogManager;
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

    public Uri createFile() {
        String fileName = installationWorkCapture.getInstallationWork().getTitle();
        String root = context.getResources().getString(R.string.installation_work_root);
        String[] directories = mapper.getInstallationWorkDirectories(root, installationWorkCapture.getInstallationWork());
        File file = processFileUseCase.createFile(installationWorkCapture.getInstallationWork(), directories, fileName);
        installationWorkCapture.setFile(file);
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
    }

    public void installationWorkCaptured() {
        installationWorkCaptureView.showLoadingView(false);
        saveFile();
    }

    private void saveFile() {
        Observable.just(installationWorkCapture.getFile())
                .subscribeOn(Schedulers.io())
                .doOnNext(file -> WaterMarker.resizeImage(file, settings.getWidth(),
                        installationWorkCapture.getInstallationWork().getTitle(), new ImageObserver()))
                .subscribe();
    }

    private void uploadFile() {
        String root = context.getResources().getString(R.string.installation_work_root);
        Observable.just(installationWorkCaptureView)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler())
                .doOnNext(t -> {
                    hideLoadingView();
                    installationWorkCaptureView.imageSuccess(R.string.installation_work_photo_saved);
                    installationWorkCaptureView.showLoadingView(true);
                })
                .subscribe(t -> uploadFile(root));
    }

    private void uploadFile(String root) {
        if (connectionTracker.isOnline()) {
            InstallationWork installationWork = installationWorkCapture.getInstallationWork();
            processFileUseCase.uploadFile(new FileUploadObservable(installationWork),
                    new ProgressObserver(installationWork), mapper.getInstallationWorkDirectories(root, installationWork),
                    installationWorkCapture.getFile());
        } else {
            exceptionLogManager.addException(new Throwable("No network connection. Image file uploading cancelled"));
            hideLoadingView();
            installationWorkCaptureView.imageSuccess(R.string.installation_work_upload_warning);
            installationWorkCaptureView.onFinish();
        }
    }

    private void uploadFailed(Throwable e) {
        exceptionLogManager.addException(e);

        hideLoadingView();
        installationWorkCaptureView.imageFailed();
    }

    private void hideLoadingView() {
        Observable.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(empty -> installationWorkCaptureView.hideLoadingView());
    }

    public void checkMemorySize() {
        if (MemoryUtil.getAvailableMemorySize() < 100) {
            installationWorkCaptureView.showMemoryCleanerDialog();
        }
    }

    public void clearMemory() {
        String root = context.getResources().getString(R.string.installation_work_root);
        File rootFolder = new File(Environment.getExternalStorageDirectory(), root);
        boolean cleaned = FileManager.clear(rootFolder);
        if (cleaned) {
            installationWorkCaptureView.memoryCleaned();
        }
    }

    private class ImageObserver extends AbsObserver {
        @Override
        public void onError(Throwable e) {
            exceptionLogManager.addException(e);

            Observable.just(installationWorkCaptureView)
                    .observeOn(postExecutionThread.getScheduler())
                    .subscribe(t -> {
                        hideLoadingView();
                        installationWorkCaptureView.imageFailed();
                    });
        }

        @Override
        public void onComplete() {
            uploadFile();
        }
    }

    private class ProgressObserver extends AbsObserver<Integer> {
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
            uploadFailed(e);
        }
    }

    private class FileUploadObservable extends AbsObserver<ResponseBody> {
        InstallationWork installationWork;

        public FileUploadObservable(InstallationWork installationWork) {
            this.installationWork = installationWork;
        }

        @Override
        public void onError(Throwable e) {
            uploadFailed(e);
        }

        @Override
        public void onComplete() {
            processFileUseCase.removeCached(installationWork);
            installationWorkCapture.clear();
            hideLoadingView();
            installationWorkCaptureView.imageSuccess(R.string.installation_work_photo_uploaded);
            installationWorkCaptureView.onFinish();
        }
    }
}
