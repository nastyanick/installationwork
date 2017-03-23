package com.nastynick.installationworks.view;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivityLoginBinding;

import javax.inject.Inject;


public class LoginActivity extends AppCompatActivity {
    @Inject SharedPreferences sharedPreferences;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    public void login(View v) {
        String login = String.valueOf(binding.login.getText());
        String password = String.valueOf(binding.password.getText());
    }
}
