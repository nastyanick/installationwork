package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.executor.PostExecutionThread;
import com.nastynick.installationworks.repository.CredentialsRepository;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public class AuthUseCase extends UseCase {

    @Inject
    protected CredentialsRepository credentialsRepository;

    @Inject
    public AuthUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void checkCredentials(DisposableObserver<ResponseBody> disposableObserver) {
        execute(disposableObserver, cloudApi.checkConnection());
    }

    public void saveCredentials(String login, String password) {
        credentialsRepository.saveCredentials(login, password);
    }

    public boolean checkCredentialsExists() {
        return credentialsRepository.checkCredentialsExists();
    }
}
