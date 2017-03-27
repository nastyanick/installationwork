package com.nastynick.installationworks.util;

import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.view.SettingsView;

import javax.inject.Inject;

public class SettingsPresenter {
    SettingsView settingsView;
    SettingsUseCase settingsUseCase;

    @Inject
    public SettingsPresenter(SettingsUseCase settingsUseCase) {
        this.settingsUseCase = settingsUseCase;
    }

    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
        settingsView.setSelected(settingsUseCase.isLowResolutionSelected());
    }

    public void resolutionChecked(boolean isLow) {
        settingsUseCase.setResolution(isLow);
    }
}
