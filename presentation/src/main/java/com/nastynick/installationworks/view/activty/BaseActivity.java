package com.nastynick.installationworks.view.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.di.app.App;
import com.nastynick.installationworks.di.app.AppComponent;

public class BaseActivity extends AppCompatActivity {

    protected void startActivity(Class activity) {
        startActivity(new Intent(this, activity));
    }

    protected void toast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(SettingsActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
