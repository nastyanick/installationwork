package com.nastynick.installationworks.di.app;

import android.content.Context;
import android.widget.Toast;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.entity.InstallationWork;
import com.nastynick.installationworks.interactor.AbsObserver;
import com.nastynick.installationworks.interactor.ProcessFileUseCase;
import com.nastynick.installationworks.mapper.InstallationWorkQrCodeMapper;
import com.nastynick.installationworks.repository.InstallationWorksRepository;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.Realm;
import okhttp3.ResponseBody;

public class BackgroundUploader {
    @Inject
    protected ProcessFileUseCase processFileUseCase;
    @Inject
    protected InstallationWorkQrCodeMapper mapper;
    @Inject
    protected Context context;
    @Inject
    protected InstallationWorksRepository installationWorksRepository;

    @Inject
    public BackgroundUploader() {
    }

    void uploadFailed() {
        Realm realm = Realm.getDefaultInstance();
        Observable.just(realm.where(InstallationWork.class).findAll())
                .flatMapIterable(urls -> urls)
                .filter(installationWork -> installationWork.getFilePath() != null)
                .subscribe(this::uploadInstallationWork);
    }

    private void uploadInstallationWork(InstallationWork installationWork) {
        Toast.makeText(context, R.string.installation_work_upload, Toast.LENGTH_SHORT).show();
        processFileUseCase.uploadFile(new AbsObserver<ResponseBody>() {
                                         @Override
                                         public void onError(Throwable e) {
                                             super.onError(e);
                                         }

                                         @Override
                                         public void onComplete() {
                                             installationWorksRepository.remove(installationWork);
                                             super.onComplete();
                                         }
                                     }, new AbsObserver<>(),
                mapper.getInstallationWorkDirectories(context.getString(R.string.installation_work_root), installationWork),
                new File(installationWork.getFilePath()));
    }
}
