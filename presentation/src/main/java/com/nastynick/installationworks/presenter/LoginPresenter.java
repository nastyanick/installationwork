package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.AuthUseCase;
import com.nastynick.installationworks.view.LoginView;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

//@PerActivity
public class LoginPresenter {
    private final AuthUseCase authUseCase;
    private LoginView loginView;

    public void setView(LoginView loginView) {
        this.loginView = loginView;
    }

    @Inject
    LoginPresenter(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    public void saveCredentials() {
        String login = loginView.login();
        String password = loginView.password();
        authUseCase.saveCredentials(login, password);

        authUseCase.checkCredentials(new LoginObservable());
    }

    private class LoginObservable extends DisposableObserver<String> {

        @Override
        public void onNext(String value) {
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
                int code = ((HttpException) e).code();
                if (HttpURLConnection.HTTP_UNAUTHORIZED == code) {
                    loginView.fail(R.string.error_auth_unauthorized);
                } else loginView.fail(R.string.error_auth);
            }

        }

        @Override
        public void onComplete() {
            loginView.success();
        }
    }
}
