package com.nastynick.installationworks.di.app;

import com.nastynick.installationworks.InstallationWorkCaptured;
import com.nastynick.installationworks.repository.InstallationWorksRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    private InstallationWorkCaptured installationWorkCaptured;

    public DataModule() {
        installationWorkCaptured = new InstallationWorkCaptured();
    }

    @Provides
    @Singleton
    public InstallationWorkCaptured installationWorkCapture() {
        return installationWorkCaptured;
    }

    @Provides
    @Singleton
    public InstallationWorksRepository installationWorksRepository() {
        return new InstallationWorksRepository();
    }
}
