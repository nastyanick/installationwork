package com.nastynick.installationworks.view.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nastynick.installationworks.di.app.App;
import com.nastynick.installationworks.di.app.BackgroundUploader;
import com.nastynick.installationworks.presenter.StarterPresenter;
import com.nastynick.installationworks.view.StarterView;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements StarterView {
    @Inject
    protected StarterPresenter starterPresenter;

    @Inject
    protected BackgroundUploader backgroundUploader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getAppComponent().inject(this);
        starterPresenter.setStarterView(this);
        starterPresenter.checkUserCredentials();
        ((App) getApplication()).registerConnectionChecker(backgroundUploader);
    }

    @Override
    public void userLoggedIn() {
        Intent intent = new Intent(this, InstallationWorkCaptureActivity.class);
        intent.putExtra(InstallationWorkCaptureActivity.NEED_MEMORY_CHECK, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void userNotLoggedIn() {
        startActivity(LoginActivity.class);
        finish();
    }
}
