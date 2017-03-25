package com.nastynick.installationworks.view.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.presenter.StarterPresenter;
import com.nastynick.installationworks.view.StarterView;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements StarterView {
    @Inject
    protected StarterPresenter starterPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getAppComponent().inject(this);
        starterPresenter.setStarterView(this);
        starterPresenter.checkUserCredentials();
    }

    @Override
    public void userLoggedIn() {
        toast(R.string.authorization_success);
    }

    @Override
    public void userNotLoggedIn() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
