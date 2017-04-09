package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.di.app.ExceptionLogManager;
import com.nastynick.installationworks.entity.CredentialsData;
import com.nastynick.installationworks.interactor.AbsObserver;
import com.nastynick.installationworks.interactor.AuthUseCase;
import com.nastynick.installationworks.mapper.CredentialsModelDataMapper;
import com.nastynick.installationworks.model.CredentialsModel;
import com.nastynick.installationworks.view.LoginView;

import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Controls communication between UI and domain level
 */
public class LoginPresenter {
    private final AuthUseCase authUseCase;
    private LoginView loginView;
    private ExceptionLogManager exceptionLogManager;
    private CredentialsModelDataMapper mapper;

    @Inject
    LoginPresenter(AuthUseCase authUseCase, ExceptionLogManager exceptionLogManager, CredentialsModelDataMapper mapper) {
        this.authUseCase = authUseCase;
        this.exceptionLogManager = exceptionLogManager;
        this.mapper = mapper;
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

    public void setCredentials() {
        authUseCase.loadCredentialsData(new CredentialsLoadObserver());
    }

    private class LoginObservable extends AbsObserver<ResponseBody> {
        @Override
        public void onError(Throwable e) {
            exceptionLogManager.addException(e);

            if (e instanceof HttpException) {
                int code = ((HttpException) e).code();
                if (HttpURLConnection.HTTP_UNAUTHORIZED == code) {
                    loginView.fail(R.string.error_auth_unauthorized);
                }
            } else if (e instanceof UnknownHostException) {
                loginView.fail(R.string.error_auth_net_failed);
            } else loginView.fail(R.string.error_auth);
        }

        @Override
        public void onComplete() {
            loginView.success();
        }
    }

    private class CredentialsLoadObserver extends AbsObserver<List<CredentialsData>> {
        @Override
        public void onNext(List<CredentialsData> data) {
            if (data != null && !data.isEmpty()) {
                CredentialsModel credentialsModel = mapper.transform(data.get(0));
                loginView.renderCredentials(credentialsModel);
                loginView.renderCredentialsList(mapper.transform(data));
            }
        }
    }
}
