package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.InstallationFileCreator;
import com.nastynick.installationworks.PostExecutionThread;

import java.io.File;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class UploadFileUseCase extends UseCase {
    @Inject
    public UploadFileUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public File createFile(String[] directories, String fileName) {
        return InstallationFileCreator.createFile(fileName, directories);
    }

    public void uploadFile(DisposableObserver<ResponseBody> uploadObserver, Observer<Integer> progressObserver, String[] directories, File imageFile) {
        uploadDirectory(directories, directories[0], 0, uploadObserver, progressObserver, imageFile);
    }

    private void uploadDirectory(String[] directories, String directoryName, int depths, DisposableObserver<ResponseBody> uploadObserver, Observer<Integer> progressObserver, File imageFile) {
        cloudApi.createDirectory(directoryName)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new UploadObserver(directories, depths, uploadObserver, progressObserver, imageFile));
    }

    private class UploadObserver extends DisposableObserver<ResponseBody> {
        String[] directories;
        int depths;
        DisposableObserver<ResponseBody> uploadObserver;
        Observer<Integer> progressObserver;
        File imageFile;

        UploadObserver(String[] directories, int depths, DisposableObserver<ResponseBody> uploadObserver, Observer<Integer> progressObserver, File imageFile) {
            this.directories = directories;
            this.depths = depths;
            this.uploadObserver = uploadObserver;
            this.progressObserver = progressObserver;
            this.imageFile = imageFile;
        }

        @Override
        public void onNext(ResponseBody value) {
        }

        @Override
        public void onError(Throwable e) {
            HttpException httpException = (HttpException) e;
            if (HttpURLConnection.HTTP_BAD_METHOD == httpException.code()) {
                createDirectory();
            }
        }

        @Override
        public void onComplete() {
            createDirectory();
        }

        private void createDirectory() {
            depths++;
            if (depths <= directories.length) {
                uploadDirectory(directories, getDirectory(depths), depths, uploadObserver, progressObserver, imageFile);
            } else execute(uploadObserver, cloudApi.uploadImage(imageFile, progressObserver,
                    postExecutionThread.getScheduler(), getDirectory(--depths)));
        }

        private String getDirectory(int depths) {
            String path = "";
            for (int i = 0; i < depths; i++) {
                path = path + "/" + directories[i];
            }
            return path;
        }
    }
}
