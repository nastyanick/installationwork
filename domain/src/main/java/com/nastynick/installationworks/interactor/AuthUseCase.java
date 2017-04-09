package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.entity.CredentialsData;
import com.nastynick.installationworks.executor.PostExecutionThread;
import com.nastynick.installationworks.repository.CredentialsRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Class AuthUseCase provides access to retrieving and storing settings user's auth credentials
 */
public class AuthUseCase extends UseCase {

    @Inject
    protected CredentialsRepository credentialsRepository;

    @Inject
    public AuthUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void checkCredentials(Observer<ResponseBody> credentialsCheckObserver) {
        execute(credentialsCheckObserver, cloudApi.checkConnection());
    }

    public void saveCredentials(String login, String password) {
        credentialsRepository.saveCredentials(login, password);
    }

    public boolean checkCredentialsExists() {
        return credentialsRepository.checkCredentialsExists();
    }

    public String login() {
        return credentialsRepository.getLogin();
    }

    public String password() {
        return credentialsRepository.getPassword();
    }

    public void loadCredentialsData(Observer<List<CredentialsData>> dataObserver) {
        execute(dataObserver, credentialsRepository.credentialsData());
    }
}
