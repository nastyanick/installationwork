package com.nastynick.installationworks.net;

import io.realm.Realm;

public class BackgroundUploader {
    public void uploadFailed() {
        Realm realm = Realm.getDefaultInstance();
//         realm.where(InstallationWorkData.class).findAll().asObservable();

    }
}
