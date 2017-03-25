package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.interactor.AuthUseCase;
import com.nastynick.installationworks.view.StarterView;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class StarterPresenter {
    @Inject
    AuthUseCase authUseCase;

    StarterView starterView;

    public void setStarterView(StarterView starterView) {
        this.starterView = starterView;
    }

    @Inject
    public StarterPresenter() {
    }

    public void checkUserCredentials() {
        if (authUseCase.checkCredentialsExists()) {
            authUseCase.checkCredentials(new StarterObserver());
        } else starterView.userNotLoggedIn();
    }

    private class StarterObserver extends DisposableObserver<String> {

        @Override
        public void onNext(String value) {
        }

        @Override
        public void onError(Throwable e) {
            starterView.userNotLoggedIn();
        }

        @Override
        public void onComplete() {
            starterView.userLoggedIn();
        }
    }
}
