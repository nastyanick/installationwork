package com.nastynick.installationworks.presenter;

import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.repository.CredentialsRepository;
import com.nastynick.installationworks.view.SettingsView;

import javax.inject.Inject;

public class SettingsPresenter {
    private SettingsView settingsView;
    private SettingsUseCase settingsUseCase;
    private CredentialsRepository credentialsRepository;

    @Inject
    public SettingsPresenter(SettingsUseCase settingsUseCase, CredentialsRepository credentialsRepository) {
        this.settingsUseCase = settingsUseCase;
        this.credentialsRepository = credentialsRepository;
    }

    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
        settingsView.setSelected(settingsUseCase.isLowResolutionSelected());
        settingsView.setGifTurned(settingsUseCase.getGifTurned());
        settingsView.setFramesPickerVisibility(settingsUseCase.getGifTurned());
        settingsView.setGifFramesCount(settingsUseCase.getGifFramesCount());
        settingsView.setSettingsAccount(credentialsRepository.getLogin());
    }

    public void resolutionChecked(boolean isLow) {
        settingsUseCase.setResolution(isLow);
    }

    public void logoutClick() {
        settingsUseCase.removeData();
    }

    public void gifEnableClick(boolean enabled) {
        settingsUseCase.setGifTurned(enabled);
        settingsView.setFramesPickerVisibility(enabled);
    }

    public void onFramesCountChanged(int value) {
        settingsUseCase.setGifFramesCount(value);
    }
}
