package com.nastynick.installationworks.di;

import android.app.Application;

public class App extends Application {
    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }

    public void buildComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
