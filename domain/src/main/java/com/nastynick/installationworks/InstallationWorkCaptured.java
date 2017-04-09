package com.nastynick.installationworks;

import com.nastynick.installationworks.entity.InstallationWork;

import java.io.File;

/**
 * Singleton class contains installationWork {@link InstallationWork} and file with photo captured
 */
public class InstallationWorkCaptured {
    private File file;
    private InstallationWork installationWork;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public InstallationWork getInstallationWork() {
        return installationWork;
    }

    public void setInstallationWork(InstallationWork installationWork) {
        this.installationWork = installationWork;
    }

    public void clear() {
        file = null;
        installationWork = null;
    }
}
