package com.nastynick.installationworks.repository;


import android.content.Context;
import android.util.Base64;

import com.nastynick.installationworks.CredentialsRepository;
import com.nastynick.installationworks.net.YandexService;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
        return yandexService.getDiskInfo(credentialsRepository.getCredentials(), "1");
    }

    public Observable<ResponseBody> uploadImageMulti(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);

//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        return yandexService.uploadFile(credentialsRepository.getCredentials(), "name.jpg", name, body);
    }


    public Observable<ResponseBody> uploadImage(File file) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);

//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return yandexService.uploadFile(credentialsRepository.getCredentials(), imageBytes.length, "name.jpg", encodedImage);
    }


}
