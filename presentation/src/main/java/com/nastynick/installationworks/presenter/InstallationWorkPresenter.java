package com.nastynick.installationworks.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.nastynick.installationworks.InstallationWork;
import com.nastynick.installationworks.PostExecutionThread;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.UploadFileUseCase;
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
    private InstallationWorkQrCodeMapper mapper;
    private InstallationWorkCaptureView installationWorkCaptureView;
    private InstallationWork installationWork;
    private File file;
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
        installationWork = mapper.transform(code);
    }

    public void installationWorkCaptured() {
        installationWorkCaptureView.showLoadingView();
        Observable.just(file.getAbsolutePath())
                .subscribeOn(Schedulers.io())
                .map(BitmapFactory::decodeFile)
                .map(bitmap -> WaterMarker.mark(bitmap, getWaterMarkTitle()))
                .doOnNext(bitmap -> InstallationFileCreator.saveBitmap(bitmap, file))
                .subscribeOn(postExecutionThread.getScheduler())
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(bitmap -> {
//                    installationWorkCaptureView.viewPhoto(bitmap);
                    installationWorkCaptureView.hideLoadingView();
                    installationWorkCaptureView.imageSuccess();
                    uploadFileUseCase.uploadFile(new InstallationFileUploadObservable(), file);
                });
    }

    private String getWaterMarkTitle() {
        return String.format(context.getResources().getString(R.string.installation_work_photo_water_mark),
                installationWork.getConstructionNumber(), installationWork.getAddress());
    }

    public File createFile() {
        String[] directories = mapper.getInstallationWorkDirectories(installationWork);
        file = InstallationFileCreator.createFile(installationWork.getConstructionNumber(),
                context.getResources().getString(R.string.installation_work_root), directories);
        return file;
    }

    private class InstallationFileUploadObservable extends DisposableObserver<ResponseBody> {

        @Override
        public void onNext(ResponseBody responseBody) {
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
//                int code = ((HttpException) e).code();
//                if (HttpURLConnection.HTTP_UNAUTHORIZED == code) {
//                    loginView.fail(R.string.error_auth_unauthorized);
//                } else loginView.fail(R.string.error_auth);
            }

        }

        @Override
        public void onComplete() {
            installationWorkCaptureView.hideLoadingView();
        }
    }
}
