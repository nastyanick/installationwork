package com.nastynick.installationworks.di;


import com.nastynick.installationworks.view.activty.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(LoginActivity loginActivity);
}
