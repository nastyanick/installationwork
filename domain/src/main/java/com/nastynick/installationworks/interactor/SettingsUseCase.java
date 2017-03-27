package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.repository.ResolutionRepository;

import javax.inject.Inject;

public class SettingsUseCase {
    ResolutionRepository resolutionRepository;

    @Inject
    public SettingsUseCase(ResolutionRepository resolutionRepository) {
        this.resolutionRepository = resolutionRepository;
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
}
