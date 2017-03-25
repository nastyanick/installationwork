package com.nastynick.installationworks.net;

import io.reactivex.Observable;
import retrofit2.http.HTTP;
import retrofit2.http.Header;

public interface YandexService {
    @HTTP(method = "PROPFIND", path = "./")
    Observable<String> getDiskInfo(@Header("Depth") String depth);
}
