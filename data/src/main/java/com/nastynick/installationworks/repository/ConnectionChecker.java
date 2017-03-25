package com.nastynick.installationworks.repository;


import com.nastynick.installationworks.net.YandexService;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ConnectionChecker {
    @Inject
    YandexService yandexService;

    @Inject
    public ConnectionChecker() {
    }

    public Observable<String> checkConnection() {
        return yandexService.getDiskInfo("1");
    }
}
