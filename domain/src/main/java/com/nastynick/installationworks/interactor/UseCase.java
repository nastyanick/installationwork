package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.api.CloudApi;
import com.nastynick.installationworks.executor.PostExecutionThread;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase {
    @Inject
    protected CloudApi cloudApi;

    @Inject
    protected PostExecutionThread postExecutionThread;

    protected <T> void execute(Observer<T> observer, Observable<T> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler())
                .subscribeWith(observer);
    }
}
