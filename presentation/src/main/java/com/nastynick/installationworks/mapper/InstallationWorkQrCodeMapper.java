package com.nastynick.installationworks.mapper;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.entity.InstallationWork;
import com.nastynick.installationworks.repository.InstallationWorksRepository;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Class that parse qr code and transform it to {@link InstallationWork}
 */
public class InstallationWorkQrCodeMapper {
    private static final String DELIMITER = "\\|+";
    private static final int ORDER_NUMBER = 0;
    private static final int CONSTRUCTION_NUMBER = 1;
    private static final int DATE = 3;
    private static final int CONTRACTOR_CODE = 4;
    private static final int MATERIAL_NAME = 5;
    private static final int ADDRESS = 6;
    @Inject
    protected InstallationWorksRepository installationWorksRepository;
    private Context context;

    @Inject
    public InstallationWorkQrCodeMapper(Context context) {
        this.context = context;
    }

    public InstallationWork transform(String qrCode) {
        try {
            String[] installationWorkFields = qrCode.split(DELIMITER);
            InstallationWork installationWork = new InstallationWork();
            installationWork.setQrCode(qrCode);
            installationWork.setYear(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            installationWork.setOrderNumber(installationWorkFields[ORDER_NUMBER]);
            installationWork.setConstructionNumber(installationWorkFields[CONSTRUCTION_NUMBER]);
            installationWork.setDate(installationWorkFields[DATE]);
            installationWork.setContractorCode(installationWorkFields[CONTRACTOR_CODE]);
            installationWork.setMaterialName(installationWorkFields[MATERIAL_NAME]);
            installationWork.setAddress(installationWorkFields[ADDRESS]);
            installationWork.setTitle(getTitle(installationWork.getConstructionNumber(), installationWork.getAddress()));
            Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealmOrUpdate(installationWork));
            return installationWork;
        } catch (RuntimeException e) {
            Crashlytics.logException(e);
            return null;
        }
    }

    /**
     * Creates title of installation work that contain
     *
     * @param constructionNumber
     * @param address
     * @return title
     */
    private String getTitle(String constructionNumber, String address) {
        address = address.replace("/", "_");
        return String.format(context.getResources().getString(R.string.installation_work_title),
                constructionNumber, address);
    }

    /**
     * Creates array of installationWork directories
     *
     * @param root             root directory
     * @param installationWork
     * @return array of directories
     */
    public String[] getInstallationWorkDirectories(String root, InstallationWork installationWork) {
        return new String[]{root, installationWork.getYear(), installationWork.getContractorCode(),
                installationWork.getOrderNumber(), installationWork.getMaterialName(),
                installationWork.getDate()};
    }
}
