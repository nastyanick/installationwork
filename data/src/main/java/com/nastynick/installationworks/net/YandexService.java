package com.nastynick.installationworks.net;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * YandexService provides requests to Yandex Disk
 */
public interface YandexService {
    @HTTP(method = "PROPFIND", path = "./")
    Observable<ResponseBody> getDiskInfo(@Header("Depth") String depth);

    @PUT("/{fileName}")
    Observable<ResponseBody> uploadFile(@Path("fileName") String fileName, @Body RequestBody image);

    @HTTP(method = "MKCOL", path = "{path}")
    Observable<ResponseBody> createDirectory(@Path("path") String directoryName);
}
