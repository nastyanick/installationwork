package com.nastynick.installationworks.interactor;

import android.content.SharedPreferences;

import com.nastynick.installationworks.PostExecutionThread;
import com.nastynick.installationworks.repository.ConnectionChecker;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AuthUseCase {

    @Inject
    protected ConnectionChecker connectionChecker;

    @Inject
    protected SharedPreferences sharedPreferences;

    @Inject
    protected PostExecutionThread postExecutionThread;

    public void checkCredentials(DisposableObserver<String> disposableObserver) {
        Observable<String> observable = connectionChecker.checkConnection();
        observable.subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler())
                .subscribeWith(disposableObserver);
    }

    @Inject
    public AuthUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void saveCredentials(String login, String password) {
        sharedPreferences.edit().putString(SharedCredentials.LOGIN, login).apply();
        sharedPreferences.edit().putString(SharedCredentials.PASSWORD, password).apply();
    }

    public boolean checkCredentialsExists() {
        return sharedPreferences.getString(SharedCredentials.LOGIN, null) != null &&
                sharedPreferences.getString(SharedCredentials.PASSWORD, null) != null;
    }
}
