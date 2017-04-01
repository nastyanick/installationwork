package com.nastynick.installationworks.repository;

import android.content.SharedPreferences;

import com.nastynick.installationworks.entity.CredentialsData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

public class CredentialsRepository {
    private static String LOGIN = "login";
    private static String PASSWORD = "password";

    SharedPreferences sharedPreferences;

    @Inject
    public CredentialsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getLogin() {
        return sharedPreferences.getString(LOGIN, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void saveCredentials(String login, String password) {
        sharedPreferences.edit().putString(CredentialsRepository.LOGIN, login).apply();
        sharedPreferences.edit().putString(CredentialsRepository.PASSWORD, password).apply();

        dbSave(login, password);
    }

    private void dbSave(String login, String password) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CredentialsData credentials = new CredentialsData();
        credentials.setPassword(password);
        credentials.setLogin(login);
        realm.copyToRealmOrUpdate(credentials);
        realm.commitTransaction();
    }

    public boolean checkCredentialsExists() {
        return sharedPreferences.getString(CredentialsRepository.LOGIN, null) != null &&
                sharedPreferences.getString(CredentialsRepository.PASSWORD, null) != null;
    }

    public Observable<List<CredentialsData>> credentialsData() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CredentialsData> credentialsResults = realm.where(CredentialsData.class).findAll();
        return Observable.fromArray(credentialsResults);
    }
}
