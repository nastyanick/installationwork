package com.nastynick.installationworks.repository;

import android.content.SharedPreferences;

import javax.inject.Inject;

import okhttp3.Credentials;

public class CredentialsRepository {
    private static String LOGIN = "login";
    private static String PASSWORD = "password";

    SharedPreferences sharedPreferences;

    @Inject
    public CredentialsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getCredentials() {
        return Credentials.basic(sharedPreferences.getString(LOGIN, ""),
                sharedPreferences.getString(PASSWORD, ""));
    }

    public void saveCredentials(String login, String password) {
        sharedPreferences.edit().putString(CredentialsRepository.LOGIN, login).apply();
        sharedPreferences.edit().putString(CredentialsRepository.PASSWORD, password).apply();
    }

    public boolean checkCredentialsExists() {
        return sharedPreferences.getString(CredentialsRepository.LOGIN, null) != null &&
                sharedPreferences.getString(CredentialsRepository.PASSWORD, null) != null;
    }

    public void clear() {
        sharedPreferences.edit().remove(CredentialsRepository.LOGIN).apply();
        sharedPreferences.edit().remove(CredentialsRepository.PASSWORD).apply();
    }
}
