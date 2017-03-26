package com.nastynick.installationworks.net;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface YandexService {
    @HTTP(method = "PROPFIND", path = "./")
    Observable<ResponseBody> getDiskInfo(@Header("Authorization") String authorization, @Header("Depth") String depth);

    @Multipart
    @PUT("/{fileName}")
    Observable<ResponseBody> uploadFile(@Header("Authorization") String authorization, @Path("fileName") String fileName, @Part("name") RequestBody description, @Part MultipartBody.Part image);

    @PUT("/{fileName}")
    Observable<ResponseBody> uploadFile(@Header("Authorization") String authorization, @Header("Content-Length") int contentLength,
                                        @Path("fileName") String fileName, @Body String image);
}
