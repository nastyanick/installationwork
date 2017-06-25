package com.nastynick.installationworks.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.nastynick.installationworks.InstallationWorkCaptured;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.di.app.ConnectionTracker;
import com.nastynick.installationworks.di.app.ExceptionLogManager;
import com.nastynick.installationworks.entity.InstallationWork;
import com.nastynick.installationworks.executor.PostExecutionThread;
import com.nastynick.installationworks.file.FileManager;
import com.nastynick.installationworks.interactor.AbsObserver;
import com.nastynick.installationworks.interactor.GifCreating;
import com.nastynick.installationworks.interactor.ProcessFileUseCase;
import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.mapper.InstallationWorkQrCodeMapper;
import com.nastynick.installationworks.repository.SettingsRepository;
import com.nastynick.installationworks.util.MemoryUtil;
import com.nastynick.installationworks.util.WaterMarker;
import com.nastynick.installationworks.view.InstallationWorkCaptureView;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Controls communication between UI and domain level
 */
public class InstallationWorkPresenter {
    protected InstallationWorkCaptured installationWorkCaptured;
    private ConnectionTracker connectionTracker;
    private InstallationWorkQrCodeMapper mapper;
    private PostExecutionThread postExecutionThread;
    private ProcessFileUseCase processFileUseCase;
    private SettingsUseCase settings;
    private Context context;
    private InstallationWorkCaptureView installationWorkCaptureView;
    private ExceptionLogManager exceptionLogManager;
    private GifCreating gifCreating;
    private SettingsRepository settingsRepository;
    private int finishedCount = 0;

    @Inject
    public InstallationWorkPresenter(InstallationWorkQrCodeMapper mapper, SettingsUseCase settings, Context context,
                                     PostExecutionThread postExecutionThread, ProcessFileUseCase processFileUseCase,
                                     InstallationWorkCaptured installationWorkCaptured, ConnectionTracker connectionTracker,
                                     ExceptionLogManager exceptionLogManager, GifCreating gifCreating,
                                     SettingsRepository settingsRepository) {
        this.mapper = mapper;
        this.settings = settings;
        this.context = context;
        this.postExecutionThread = postExecutionThread;
        this.processFileUseCase = processFileUseCase;
        this.installationWorkCaptured = installationWorkCaptured;
        this.connectionTracker = connectionTracker;
        this.exceptionLogManager = exceptionLogManager;
        this.gifCreating = gifCreating;
        this.settingsRepository = settingsRepository;
    }

    public void setInstallationWorkCaptureView(InstallationWorkCaptureView installationWorkCaptureView) {
        this.installationWorkCaptureView = installationWorkCaptureView;
    }

    public void transformCodeToInstallationWork(String code) {
        InstallationWork transform = mapper.transform(code);
        if (transform == null) {
            installationWorkCaptureView.qrCodeFailed();
            installationWorkCaptureView.onFinish();
        } else installationWorkCaptured.setInstallationWork(transform);
    }

    public Uri getUriFile() {
        File file = createFile(ProcessFileUseCase.JPG_EXTENSION);
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
    }

    public File createFile(String extension) {
        String fileName = installationWorkCaptured.getInstallationWork().getTitle();
        String root = context.getResources().getString(R.string.installation_work_root);
        String[] directories = mapper.getInstallationWorkDirectories(root, installationWorkCaptured.getInstallationWork());
        File file = processFileUseCase.createFile(installationWorkCaptured.getInstallationWork(), directories, fileName, extension);
        installationWorkCaptured.setFile(file);
        return file;
    }

    public void installationWorkCaptured() {
        installationWorkCaptureView.showLoadingView(false);
        saveFile();
    }

    /**
     * Process and saves image from file
     */
    private void saveFile() {
        Observable.just(installationWorkCaptured.getFile())
                .subscribeOn(Schedulers.io())
                .doOnNext(file -> WaterMarker.createImage(file, settings.getWidth(),
                        installationWorkCaptured.getInstallationWork().getTitle(), new ImageObserver()))
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

    /**
     * Uploads processed image to server
     */
    private void uploadFile(String root) {
        if (connectionTracker.isOnline()) {
            InstallationWork installationWork = installationWorkCaptured.getInstallationWork();
            processFileUseCase.uploadFile(new FileUploadObserver(installationWork),
                    new ProgressObserver(installationWork), mapper.getInstallationWorkDirectories(root, installationWork),
                    installationWorkCaptured.getFile());
        } else {
            exceptionLogManager.addException(new Throwable("No network connection. Image file uploading cancelled"));
            hideLoadingView();
            installationWorkCaptureView.imageSuccess(R.string.installation_work_upload_warning);
            installationWorkCaptureView.onFinish();
        }
    }

    private void uploadFailed(Throwable e) {
//        exceptionLogManager.addException(e);

        Log.i("UploadFailed", e.getMessage());
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

    public void clearTemp() {
        String root = context.getResources().getString(R.string.installation_work_root);
        File tempFolder = new File(Environment.getExternalStorageDirectory(), root + "/temp");
        FileManager.clear(tempFolder);
    }

    public SettingsRepository settings() {
        return settingsRepository;
    }

    public void burstTaken(String[] burst) {
        installationWorkCaptureView.showLoadingView(false);
        installationWorkCaptureView.setGifLoadingDialogTitle();
        Observable.fromArray(burst)
                .flatMap(fileName ->
                        Observable.just(fileName)
                                .map(File::new)
                                .doOnNext(file -> WaterMarker.createImage(file,
                                        settings.getGifWidth(),
                                        installationWorkCaptured.getInstallationWork().getTitle(),
                                        new SimpleBitmapMarkerObserver(burst))))
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void onError(Throwable e, Consumer<InstallationWorkCaptureView> consumer) {
        exceptionLogManager.addException(e);

        Observable.just(installationWorkCaptureView)
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(consumer);
    }

    private class SimpleBitmapMarkerObserver extends AbsObserver {

        private String[] burst;

        public SimpleBitmapMarkerObserver(String[] burst) {
            this.burst = burst;
        }

        @Override
        public void onComplete() {
            finishedCount++;
            if (finishedCount >= burst.length) {
                gifCreating.makeGif(createFile(ProcessFileUseCase.GIF_EXTENSION), burst, new GifUploadObserver());
            }
        }
    }

    private class GifUploadObserver extends AbsObserver<File> {
        @Override
        public void onNext(File file) {
            installationWorkCaptured.setFile(file);
            uploadFile();
            clearTemp();
        }

        @Override
        public void onError(Throwable e) {
            InstallationWorkPresenter.this.onError(e, view -> {
                view.hideLoadingView();
                view.showGifFailedMessage();
            });
        }
    }

    /**
     * Observes image processing
     */
    private class ImageObserver extends AbsObserver {
        @Override
        public void onError(Throwable e) {
            InstallationWorkPresenter.this.onError(e, view -> {
                view.hideLoadingView();
                view.imageFailed();
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

    private class FileUploadObserver extends AbsObserver<ResponseBody> {
        InstallationWork installationWork;

        public FileUploadObserver(InstallationWork installationWork) {
            this.installationWork = installationWork;
        }

        @Override
        public void onError(Throwable e) {
            uploadFailed(e);
        }

        @Override
        public void onComplete() {
            processFileUseCase.removeCached(installationWork);
            installationWorkCaptured.clear();
            hideLoadingView();
            installationWorkCaptureView.imageSuccess(R.string.installation_work_photo_uploaded);
            installationWorkCaptureView.onFinish();
        }
    }
}
