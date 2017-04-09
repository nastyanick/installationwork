package com.nastynick.installationworks.presenter;

import android.content.Context;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.interactor.SettingsUseCase;
import com.nastynick.installationworks.repository.CredentialsRepository;
import com.nastynick.installationworks.view.SettingsView;

import javax.inject.Inject;

public class SettingsPresenter {
    SettingsView settingsView;
    SettingsUseCase settingsUseCase;
    Context context;
    private CredentialsRepository credentialsRepository;

    @Inject
    public SettingsPresenter(SettingsUseCase settingsUseCase, Context context, CredentialsRepository credentialsRepository) {
        this.settingsUseCase = settingsUseCase;
        this.context = context;
        this.credentialsRepository = credentialsRepository;
    }

    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
        settingsView.setSelected(settingsUseCase.isLowResolutionSelected());
    }

    public void setSettingAccout() {
        String title = context.getString(R.string.settings_pattern, context.getString(R.string.settings_logout), credentialsRepository.getLogin());
        settingsView.setSettingsAccount(title);
    }

    public void resolutionChecked(boolean isLow) {
        settingsUseCase.setResolution(isLow);
    }

    public void logout() {
        settingsUseCase.removeData();
    }
}
