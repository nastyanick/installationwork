package com.nastynick.installationworks.view.activty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivityLoginBinding;
import com.nastynick.installationworks.presenter.LoginPresenter;
import com.nastynick.installationworks.view.LoginView;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginView {
    @Inject
    protected LoginPresenter loginPresenter;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginPresenter.setView(this);
    }

    public void login(View v) {
        loginPresenter.saveCredentials();
    }

    @Override
    public String login() {
        return String.valueOf(binding.login.getText());
    }

    @Override
    public String password() {
        return String.valueOf(binding.password.getText());
    }

    @Override
    public void success() {
        toast(R.string.authorization_success);
        startActivity(InstallationWorkCaptureActivity.class);
        finish();
    }

    @Override
    public void fail(int mesId) {
        toast(mesId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
