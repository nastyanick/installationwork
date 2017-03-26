package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.AuthUseCase;
import com.nastynick.installationworks.view.LoginView;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

//@PerActivity
public class LoginPresenter {
    private final AuthUseCase authUseCase;
    private LoginView loginView;

    @Inject
    LoginPresenter(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    public void setView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void saveCredentials() {
        String login = loginView.login();
        String password = loginView.password();
        authUseCase.saveCredentials(login, password);

        authUseCase.checkCredentials(new LoginObservable());
    }

    private class LoginObservable extends DisposableObserver<ResponseBody> {

        @Override
        public void onNext(ResponseBody responseBody) {
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
