package com.nastynick.installationworks.mapper;

import com.nastynick.installationworks.InstallationWork;

import java.util.Calendar;

import javax.inject.Inject;

public class InstallationWorkQrCodeMapper {
    private static final String DELIMITER = "\\|+";
    private static final int ORDER_NUMBER = 0;
    private static final int CONSTRUCTION_NUMBER = 1;
    private static final int DATE = 3;
    private static final int CONTRACTOR_CODE = 4;
    private static final int MATERIAL_NAME = 5;
    private static final int ADDRESS = 6;

    @Inject
    public InstallationWorkQrCodeMapper() {
    }

    public InstallationWork transform(String qrCode) {
        String[] installationWorkFields = qrCode.split(DELIMITER);
        InstallationWork installationWork = new InstallationWork();
        installationWork.setYear(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        installationWork.setOrderNumber(installationWorkFields[ORDER_NUMBER]);
        installationWork.setConstructionNumber(installationWorkFields[CONSTRUCTION_NUMBER]);
        installationWork.setDate(installationWorkFields[DATE]);
        installationWork.setContractorCode(installationWorkFields[CONTRACTOR_CODE]);
        installationWork.setMaterialName(installationWorkFields[MATERIAL_NAME]);
        installationWork.setAddress(installationWorkFields[ADDRESS]);
        return installationWork;
    }

    public String[] getInstallationWorkDirectories(InstallationWork installationWork) {
        return new String[]{installationWork.getYear(), installationWork.getContractorCode(),
                installationWork.getOrderNumber(), installationWork.getMaterialName(),
                installationWork.getDate()};
    }
}
