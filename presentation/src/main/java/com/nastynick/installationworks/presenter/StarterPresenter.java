package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.interactor.AuthUseCase;
import com.nastynick.installationworks.view.StarterView;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public class StarterPresenter {
    AuthUseCase authUseCase;

    StarterView starterView;

    @Inject
    public StarterPresenter(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
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
            starterView.userNotLoggedIn();
        }

        @Override
        public void onComplete() {
            starterView.userLoggedIn();
        }
    }
}
