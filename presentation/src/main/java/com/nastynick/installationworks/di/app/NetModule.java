package com.nastynick.installationworks.di.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nastynick.installationworks.net.BackgroundUploader;
import com.nastynick.installationworks.net.YandexService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;
import retrofit2.Retrofit;

@Module
public class NetModule {

    private BackgroundUploader backgroundUploader;

    public NetModule(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .rxFactory(new RealmObservableFactory())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        context.registerReceiver(new ConnectivityChangeReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Provides
    @Singleton
    public YandexService yandexService(Retrofit retrofit) {
        return retrofit.create(YandexService.class);
    }

    private class ConnectivityChangeReceiver extends BroadcastReceiver {
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
