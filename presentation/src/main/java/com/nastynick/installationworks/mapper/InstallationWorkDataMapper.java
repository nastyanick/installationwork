package com.nastynick.installationworks.mapper;

import com.nastynick.installationworks.InstallationWork;
import com.nastynick.installationworks.model.InstallationWorkData;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class InstallationWorkDataMapper {
    @Inject
    public InstallationWorkDataMapper() {
    }

    public void saveToData(InstallationWork installationWork) {
        InstallationWorkData data = new InstallationWorkData();
        data.setQrCode(installationWork.getQrCode());
        data.setYear(installationWork.getYear());
        data.setOrderNumber(installationWork.getOrderNumber());
        data.setConstructionNumber(installationWork.getConstructionNumber());
        data.setDate(installationWork.getDate());
        data.setContractorCode(installationWork.getContractorCode());
        data.setMaterialName(installationWork.getMaterialName());
        data.setAddress(installationWork.getAddress());
        data.setTitle(installationWork.getTitle());

        Realm realm = Realm.getDefaultInstance();
        RealmResults<InstallationWorkData> works = realm.where(InstallationWorkData.class).findAll();
    }
}
