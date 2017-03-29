package com.nastynick.installationworks.di.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import io.fabric.sdk.android.Fabric;

public class App extends Application {
    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        buildComponent();
    }

    public void registerConnectionChecker(BackgroundUploader backgroundUploader) {
        registerReceiver(new ConnectivityChangeReceiver(backgroundUploader), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void buildComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(this))
                .dataModule(new DataModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private class ConnectivityChangeReceiver extends BroadcastReceiver {
        private BackgroundUploader backgroundUploader;

        public ConnectivityChangeReceiver(BackgroundUploader backgroundUploader) {

            this.backgroundUploader = backgroundUploader;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                backgroundUploader.uploadFailed();
            }
        }
    }
}
