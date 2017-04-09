package com.nastynick.installationworks.interactor;

import com.nastynick.installationworks.entity.InstallationWork;
import com.nastynick.installationworks.executor.PostExecutionThread;
import com.nastynick.installationworks.file.FileManager;
import com.nastynick.installationworks.repository.InstallationWorksRepository;

import java.io.File;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * ProcessFileUseCase represents a use case for
 * processing  and uploading files
 */
public class ProcessFileUseCase extends UseCase {
    private InstallationWorksRepository installationWorksRepository;

    @Inject
    public ProcessFileUseCase(PostExecutionThread postExecutionThread, InstallationWorksRepository installationWorksRepository) {
        this.installationWorksRepository = installationWorksRepository;
        this.postExecutionThread = postExecutionThread;
    }

    public File createFile(InstallationWork installationWork, String[] directories, String fileName) {
        File file = FileManager.createFile(fileName, directories);
        installationWorksRepository.save(installationWork, file.getAbsolutePath());
        return file;
    }

    public void uploadFile(Observer<ResponseBody> uploadObserver, Observer<Integer> progressObserver, String[] directories, File imageFile) {
        uploadDirectory(directories, directories[0], 0, uploadObserver, progressObserver, imageFile);
    }

    private void uploadDirectory(String[] directories, String directoryName, int depths, Observer<ResponseBody> uploadObserver, Observer<Integer> progressObserver, File imageFile) {
        cloudApi.createDirectory(directoryName)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DirectoryObserver(directories, depths, uploadObserver, progressObserver, imageFile));
    }

    public void removeCached(InstallationWork installationWork) {
        installationWorksRepository.removeCached(installationWork);
    }

    private class DirectoryObserver extends DisposableObserver<ResponseBody> {
        String[] directories;
        int depths;
        Observer<ResponseBody> uploadObserver;
        Observer<Integer> progressObserver;
        File imageFile;

        DirectoryObserver(String[] directories, int depths, Observer<ResponseBody> uploadObserver, Observer<Integer> progressObserver, File imageFile) {
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
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                if (HttpURLConnection.HTTP_BAD_METHOD == httpException.code()) {
                    createDirectory();
                }
            } else uploadObserver.onError(e);
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
