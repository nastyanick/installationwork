package com.nastynick.installationworks.view;

public interface LoginView {
    String login();

    String password();

    void success();

    void fail(int message);
}
