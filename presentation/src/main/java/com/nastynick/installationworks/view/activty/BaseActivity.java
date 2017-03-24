package com.nastynick.installationworks.view.activty;

import android.support.v7.app.AppCompatActivity;

import com.nastynick.installationworks.di.App;
import com.nastynick.installationworks.di.AppComponent;

public class BaseActivity extends AppCompatActivity {

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }
}
