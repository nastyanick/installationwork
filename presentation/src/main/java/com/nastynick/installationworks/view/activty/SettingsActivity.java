package com.nastynick.installationworks.view.activty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivitySettingsBinding;
import com.nastynick.installationworks.di.app.ExceptionLogManager;
import com.nastynick.installationworks.repository.CredentialsRepository;
import com.nastynick.installationworks.util.SettingsPresenter;
import com.nastynick.installationworks.view.SettingsView;

import java.io.File;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity implements SettingsView {
    @Inject
    CredentialsRepository credentialsRepository;

    @Inject
    SettingsPresenter settingsPresenter;

    @Inject
    ExceptionLogManager exceptionLogManager;

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.settings);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        getAppComponent().inject(this);
        settingsPresenter.setSettingsView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLogoutClick(View view) {
        credentialsRepository.clear();
        startActivity(LoginActivity.class);
        finish();
    }

    public void onSendLogFileClick(View view) {
        File file = exceptionLogManager.getExceptionsFile();
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_GET_CONTENT);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("file/*");
        startActivity(sendIntent);
    }

    public void highResolutionChecked(View view) {
        settingsPresenter.resolutionChecked(false);
    }

    public void lowResolutionChecked(View view) {
        settingsPresenter.resolutionChecked(true);
    }

    @Override
    public void setSelected(boolean lowSelected) {
        binding.radioGroup.check(lowSelected ? R.id.radioButtonLow : R.id.radioButtonHigh);
    }
}
