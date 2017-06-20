package com.nastynick.installationworks.view.activty;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivitySettingsBinding;
import com.nastynick.installationworks.di.app.ExceptionLogManager;
import com.nastynick.installationworks.presenter.SettingsPresenter;
import com.nastynick.installationworks.view.SettingsView;

import java.io.File;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity implements SettingsView {
    private static final int MAX_FRAMES = 15;
    private static final int MIN_FRAMES = 3;

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
        initListeners();
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        settingsPresenter.setSettingsView(this);
    }

    private void initViews() {
        binding.framesPicker.setMinValue(MIN_FRAMES);
        binding.framesPicker.setMaxValue(MAX_FRAMES);
    }

    private void initListeners() {
        binding.gifSwitch.setOnCheckedChangeListener((compoundButton, checked) -> settingsPresenter.gifEnableClick(checked));
        binding.framesPicker.setOnValueChangedListener((numberPicker, oldValue, newValue) -> settingsPresenter.onFramesCountChanged(newValue));
    }

    @Override
    public void setSettingsAccount(String login) {
        String title = getString(R.string.settings_pattern, getString(R.string.settings_logout), login);
        binding.logout.setText(title);
    }

    @Override
    public void setGifTurned(boolean turned) {
        binding.gifSwitch.setChecked(turned);
    }

    @Override
    public void setGifFramesCount(int value) {
        binding.framesPicker.setValue(value);
    }

    @Override
    public void setFramesPickerVisibility(boolean visible) {
        binding.framesPickerContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
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
        settingsPresenter.logoutClick();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onSendLogFileClick(View view) {
        File file = exceptionLogManager.getExceptionsFile();
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("file/*");
        try {
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Crashlytics.logException(e);
        }
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
