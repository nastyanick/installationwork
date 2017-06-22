package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.repository.InstallationWorksRepository;
import com.nastynick.installationworks.repository.ResolutionRepository;

import javax.inject.Inject;

/**
 * Class SettingsUseCase provides access to retrieving and storing settings
 */
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

    public int getGifWidth() {
        return resolutionRepository.getGifWidth();
    }

    public boolean getGifTurned() {
        return resolutionRepository.isGifTurned();
    }

    public void setGifTurned(boolean turned) {
        resolutionRepository.setGifTurned(turned);
    }

    public void removeData() {
        installationWorksRepository.removeAll();
        resolutionRepository.remove();
    }

    public int getGifFramesCount() {
        return resolutionRepository.getGifGFramesCount();
    }

    public void setGifFramesCount(int value) {
        resolutionRepository.setGifFramesCount(value);
    }
}
