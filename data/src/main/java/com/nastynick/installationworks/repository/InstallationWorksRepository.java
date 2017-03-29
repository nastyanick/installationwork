package com.nastynick.installationworks.repository;

import com.nastynick.installationworks.model.InstallationWorkData;

import javax.inject.Inject;

public class InstallationWorksRepository {
    @Inject
    public InstallationWorksRepository() {
    }

    public void remove(InstallationWorkData installationWorkData) {
        installationWorkData.deleteFromRealm();
    }
}
