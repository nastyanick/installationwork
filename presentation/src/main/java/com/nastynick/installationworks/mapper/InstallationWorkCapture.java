package com.nastynick.installationworks.mapper;

import android.net.Uri;

import com.nastynick.installationworks.InstallationWork;

import java.io.File;

//TODO replace with realm data
public class InstallationWorkCapture {
    private Uri uri;
    private File file;
    private InstallationWork installationWork;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

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
}
