package com.nastynick.installationworks.di;

import android.content.Context;

import com.nastynick.installationworks.presenter.LoginPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    public LoginPresenter provideLoginPresenter() {
        return new LoginPresenter();
    }
}
