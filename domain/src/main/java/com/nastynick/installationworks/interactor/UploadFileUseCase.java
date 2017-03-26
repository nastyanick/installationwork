package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.PostExecutionThread;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public class UploadFileUseCase extends UseCase {
    @Inject
    public UploadFileUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void uploadFile(DisposableObserver<ResponseBody> disposableObserver, File imageFile) {
//        execute(disposableObserver, cloudApi.uploadImage(imageFile));
    }
}
