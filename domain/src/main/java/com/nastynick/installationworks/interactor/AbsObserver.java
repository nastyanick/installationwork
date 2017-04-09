package com.nastynick.installationworks.interactor;

import io.reactivex.observers.DisposableObserver;

/**
 * Abstract observer
 *
 * @param <T> the received value type
 */
public class AbsObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T value) {
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
