package com.nastynick.installationworks.model;

import io.realm.RealmObject;

public class InstallationWorkData extends RealmObject {
    String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
