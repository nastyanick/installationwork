package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.repository.InstallationWorksRepository;
import com.nastynick.installationworks.repository.SettingsRepository;

import javax.inject.Inject;

/**
 * Class SettingsUseCase provides access to retrieving and storing settings
 */
public class SettingsUseCase {
    private SettingsRepository settingsRepository;
    private InstallationWorksRepository installationWorksRepository;

    @Inject
    public SettingsUseCase(SettingsRepository settingsRepository, InstallationWorksRepository installationWorksRepository) {
        this.settingsRepository = settingsRepository;
        this.installationWorksRepository = installationWorksRepository;
    }

    public boolean isLowResolutionSelected() {
        return settingsRepository.isLow();
    }

    public void setResolution(boolean isLow) {
        settingsRepository.setResolution(isLow);
    }

    public int getWidth() {
        return settingsRepository.width();
    }

    public int getGifWidth() {
        return settingsRepository.getGifWidth();
    }

    public boolean getGifTurned() {
        return settingsRepository.isGifTurned();
    }

    public void setGifTurned(boolean turned) {
        settingsRepository.setGifTurned(turned);
    }

    public void removeData() {
        installationWorksRepository.removeAll();
        settingsRepository.remove();
    }

    public int getGifFramesCount() {
        return settingsRepository.getGifGFramesCount();
    }

    public void setGifFramesCount(int value) {
        settingsRepository.setGifFramesCount(value);
    }

    public int getFramesDelay() {
        return settingsRepository.getFramesDelay();
    }

    public void setFramesDelay(int delay) {
        settingsRepository.setFramesDelay(delay);
    }
}
