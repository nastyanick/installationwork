package com.nastynick.installationworks;

import java.io.File;

//TODO replace with realm data
public class InstallationWorkCapture {
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
