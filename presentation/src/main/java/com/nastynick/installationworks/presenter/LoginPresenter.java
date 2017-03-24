package com.nastynick.installationworks.presenter;

import android.content.SharedPreferences;
import android.util.Base64;

import com.nastynick.installationworks.view.LoginView;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

public class LoginPresenter {

    @Inject
    SharedPreferences sharedPreferences;

    private LoginView loginView;

    public void setView(LoginView loginView) {
        this.loginView = loginView;
    }

    @Inject
    public void inject() {

    }

    public void initialize() {
        String authToken = getAuthToken();
        sharedPreferences.edit().putString("authToken", authToken);
    }

    private String getAuthToken() {
        String login = loginView.login();
        String password = loginView.password();
        try {
            return new String(Base64.decode(login + ":" + password, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }
}
