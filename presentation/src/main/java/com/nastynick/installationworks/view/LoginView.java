package com.nastynick.installationworks.view;

import com.nastynick.installationworks.model.CredentialsModel;

import java.util.List;

public interface LoginView {
    String login();

    String password();

    void success();

    void fail(int message);

    void renderCredentialsList(List<CredentialsModel> data);

    void renderCredentials(CredentialsModel credentialsModel);
}
