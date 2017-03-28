package com.nastynick.installationworks.api;


import android.content.Context;

import com.nastynick.installationworks.net.ProgressRequestBody;
import com.nastynick.installationworks.net.YandexService;
import com.nastynick.installationworks.repository.CredentialsRepository;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class CloudApi {
    @Inject
    YandexService yandexService;

    @Inject
    CredentialsRepository credentialsRepository;

    @Inject
    Context context;

    @Inject
    public CloudApi() {
    }

    public Observable<ResponseBody> checkConnection() {
        return yandexService.getDiskInfo("1");
    }

    public Observable<ResponseBody> uploadImage(File file, Observer<Integer> progressObserver, Scheduler scheduler, String path) {
        RequestBody imageBody = new ProgressRequestBody(file, progressObserver, scheduler);
        String fileName = path + "/" + file.getName();
        return yandexService.uploadFile(fileName, imageBody);
    }

    public Observable<ResponseBody> createDirectory(String directoryName) {
        return yandexService.createDirectory(directoryName);
    }
}
