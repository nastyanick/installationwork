package com.nastynick.installationworks.view.activty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivityLoginBinding;
import com.nastynick.installationworks.presenter.LoginPresenter;
import com.nastynick.installationworks.view.LoginView;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginView {
    @Inject
    LoginPresenter loginPresenter;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.getAppComponent().inject(this);
        loginPresenter.setView(this);
    }

    public void login(View v) {
        loginPresenter.initialize();
    }

    @Override
    public String login() {
        return String.valueOf(binding.login.getText());
    }

    @Override
    public String password() {
        return String.valueOf(binding.password.getText());
    }
}
