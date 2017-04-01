package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.repository.InstallationWorksRepository;
import com.nastynick.installationworks.repository.ResolutionRepository;

import javax.inject.Inject;

public class SettingsUseCase {
    private ResolutionRepository resolutionRepository;
    private InstallationWorksRepository installationWorksRepository;

    @Inject
    public SettingsUseCase(ResolutionRepository resolutionRepository, InstallationWorksRepository installationWorksRepository) {
        this.resolutionRepository = resolutionRepository;
        this.installationWorksRepository = installationWorksRepository;
    }

    public boolean isLowResolutionSelected() {
        return resolutionRepository.isLow();
    }

    public void setResolution(boolean isLow) {
        resolutionRepository.setResolution(isLow);
    }

    public int getWidth() {
        return resolutionRepository.width();
    }

    public void removeData() {
        installationWorksRepository.removeAll();
        resolutionRepository.remove();
    }
}
