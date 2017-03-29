package com.nastynick.installationworks.repository;

import com.nastynick.installationworks.entity.InstallationWork;

import io.realm.Realm;

public class InstallationWorksRepository {

    public void remove(InstallationWork installationWork) {
        installationWork.deleteFromRealm();
    }

    public void save(InstallationWork installationWork) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(installationWork);
        realm.commitTransaction();
    }
}
