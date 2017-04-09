package com.nastynick.installationworks.api;


import com.nastynick.installationworks.net.ProgressRequestBody;
import com.nastynick.installationworks.net.YandexService;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * CloudApi class provides interaction with cloud service
 */
public class CloudApi {
    YandexService yandexService;

    /**
     * Constructor
     *
     * @param yandexService the service {@link  YandexService }
     */
    @Inject
    public CloudApi(YandexService yandexService) {
        this.yandexService = yandexService;
    }

    /**
     * Sends request to cloud to check connection and user's credentials
     */
    public Observable<ResponseBody> checkConnection() {
        return yandexService.getDiskInfo("1");
    }

    /**
     * Uploads file with specific path to server
     *
     * @param file             the   file to upload
     * @param progressObserver the observer that observes uploading progress
     * @param scheduler        the {@link Scheduler} to perform subscription actions on
     * @param path             the path to file on cloud service
     * @return the observable with response
     */
    public Observable<ResponseBody> uploadImage(File file, Observer<Integer> progressObserver, Scheduler scheduler, String path) {
        RequestBody imageBody = new ProgressRequestBody(file, progressObserver, scheduler);
        String fileName = path + "/" + file.getName();
        return yandexService.uploadFile(fileName, imageBody);
    }

    /**
     * Creates directory on cloud service
     *
     * @param directoryName name of directory to create on cloud service
     * @return the observable with response
     */
    public Observable<ResponseBody> createDirectory(String directoryName) {
        return yandexService.createDirectory(directoryName);
    }
}
