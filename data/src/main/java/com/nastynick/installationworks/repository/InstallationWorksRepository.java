package com.nastynick.installationworks.repository;

import com.nastynick.installationworks.entity.InstallationWork;

import io.realm.Realm;

public class InstallationWorksRepository {

    public void remove(InstallationWork installationWork) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (installationWork.isValid()) {
            installationWork.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public void save(InstallationWork installationWork, String filePath) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        installationWork.setFilePath(filePath);
        realm.copyToRealmOrUpdate(installationWork);
        realm.commitTransaction();
    }

    public void removeCached(InstallationWork installationWorkCached) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        InstallationWork installationWork = realm.where(InstallationWork.class).equalTo("qrCode",
                installationWorkCached.getQrCode()).findFirst();
        if (installationWork != null) {
            installationWork.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public void removeAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(InstallationWork.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
