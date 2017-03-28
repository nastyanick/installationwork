package com.nastynick.installationworks.di.app;

import com.nastynick.installationworks.InstallationWorkCapture;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    private InstallationWorkCapture installationWorkCapture;

    public DataModule() {
        installationWorkCapture = new InstallationWorkCapture();
    }

    @Provides
    @Singleton
    public InstallationWorkCapture installationWorkCapture() {
        return installationWorkCapture;
    }
}
