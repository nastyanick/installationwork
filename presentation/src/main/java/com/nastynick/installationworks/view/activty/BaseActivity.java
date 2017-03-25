package com.nastynick.installationworks.view.activty;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nastynick.installationworks.di.app.App;
import com.nastynick.installationworks.di.app.AppComponent;

public class BaseActivity extends AppCompatActivity {

    protected void toast(int mesId) {
        Toast.makeText(this, mesId, Toast.LENGTH_LONG).show();
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }
}
