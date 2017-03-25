package com.nastynick.installationworks.di.app;

import com.nastynick.installationworks.net.YandexService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class NetModule {

    public NetModule() {
    }

    @Provides
    @Singleton
    public YandexService yandexService(Retrofit retrofit) {
        return retrofit.create(YandexService.class);
    }
}
