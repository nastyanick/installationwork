package com.nastynick.installationworks.repository;

import com.nastynick.installationworks.entity.InstallationWork;

import io.realm.Realm;

/**
 * Class InstallationWorksRepository provides access to installation works storage
 */
public class InstallationWorksRepository {

    public void remove(InstallationWork installationWork) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            if (installationWork.isValid()) {
                installationWork.deleteFromRealm();
            }
        });
    }

    public void save(InstallationWork installationWork, String filePath) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            installationWork.setFilePath(filePath);
            realm.copyToRealmOrUpdate(installationWork);
        });
    }

    public void removeCached(InstallationWork installationWorkCached) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            InstallationWork installationWork = realm.where(InstallationWork.class).equalTo("qrCode",
                    installationWorkCached.getQrCode()).findFirst();
            if (installationWork != null) {
                installationWork.deleteFromRealm();
            }
        });
    }

    public void removeAll() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.where(InstallationWork.class).findAll().deleteAllFromRealm());
    }
}
