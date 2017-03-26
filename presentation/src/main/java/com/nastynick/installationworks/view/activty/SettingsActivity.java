package com.nastynick.installationworks.view.activty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nastynick.installationworks.CredentialsRepository;
import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivitySettingsBinding;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {
    @Inject
    CredentialsRepository credentialsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.settings);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        getAppComponent().inject(this);
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
}
