package com.nastynick.installationworks.di.app;


import com.nastynick.installationworks.view.activty.LoginActivity;
import com.nastynick.installationworks.view.activty.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(SplashActivity splashActivity);
}
