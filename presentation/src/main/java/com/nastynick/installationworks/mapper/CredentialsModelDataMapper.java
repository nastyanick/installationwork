package com.nastynick.installationworks.mapper;

import com.nastynick.installationworks.entity.CredentialsData;
import com.nastynick.installationworks.model.CredentialsModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CredentialsModelDataMapper {
    @Inject
    public CredentialsModelDataMapper() {
    }

    public CredentialsModel transform(CredentialsData credentialsData) {
        CredentialsModel model = new CredentialsModel();
        model.login = credentialsData.getLogin();
        model.password = credentialsData.getPassword();
        return model;
    }

    public List<CredentialsModel> transform(List<CredentialsData> data) {
        List<CredentialsModel> model = new ArrayList<>(data.size());
        for (CredentialsData credentialsData : data) {
            model.add(transform(credentialsData));
        }
        return model;
    }
}
