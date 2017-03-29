package com.nastynick.installationworks.di.app;

import android.content.Context;

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

    public NetModule(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .rxFactory(new RealmObservableFactory())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Provides
    @Singleton
    public YandexService yandexService(Retrofit retrofit) {
        return retrofit.create(YandexService.class);
    }

    @Provides
    @Singleton
    public ConnectionTracker connectionTracker(Context context) {
        return new ConnectionTracker(context);
    }
}
