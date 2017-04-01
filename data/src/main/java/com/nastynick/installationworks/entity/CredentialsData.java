package com.nastynick.installationworks.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CredentialsData extends RealmObject {
    @PrimaryKey
    private String login;

    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
