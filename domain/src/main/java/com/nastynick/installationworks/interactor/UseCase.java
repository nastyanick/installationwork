package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.PostExecutionThread;
import com.nastynick.installationworks.repository.CloudApi;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public abstract class UseCase {
    @Inject
    protected CloudApi cloudApi;

    @Inject
    protected PostExecutionThread postExecutionThread;

    protected void execute(DisposableObserver<ResponseBody> disposableObserver, Observable<ResponseBody> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler())
                .subscribeWith(disposableObserver);
    }
}
