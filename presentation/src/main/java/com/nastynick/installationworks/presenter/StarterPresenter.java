package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.di.app.ExceptionLogManager;
import com.nastynick.installationworks.interactor.AuthUseCase;
import com.nastynick.installationworks.view.StarterView;

import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.net.ssl.SSLException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Controls communication between UI and domain level
 */
public class StarterPresenter {
    private AuthUseCase authUseCase;
    private StarterView starterView;
    private ExceptionLogManager exceptionLogManager;

    @Inject
    public StarterPresenter(AuthUseCase authUseCase, ExceptionLogManager exceptionLogManager) {
        this.authUseCase = authUseCase;
        this.exceptionLogManager = exceptionLogManager;
    }

    public void setStarterView(StarterView starterView) {
        this.starterView = starterView;
    }

    public void checkUserCredentials() {
        if (authUseCase.checkCredentialsExists()) {
            authUseCase.checkCredentials(new StarterObserver());
        } else starterView.userNotLoggedIn();
    }

    private class StarterObserver extends DisposableObserver<ResponseBody> {

        @Override
        public void onNext(ResponseBody responseBody) {
        }

        @Override
        public void onError(Throwable e) {
            exceptionLogManager.addException(e);

            if (e instanceof UnknownHostException || e instanceof SSLException) {
                starterView.userLoggedIn();
            } else starterView.userNotLoggedIn();
        }

        @Override
        public void onComplete() {
            starterView.userLoggedIn();
        }
    }
}
