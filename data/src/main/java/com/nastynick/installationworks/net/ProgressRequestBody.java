package com.nastynick.installationworks.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Class ProgressRequestBody provides tracking upload progress
 */
public class ProgressRequestBody extends RequestBody {
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private File file;
    private Observer<Integer> progressObserver;
    private Observable observable;

    public ProgressRequestBody(File file, Observer<Integer> progressObserver, Scheduler scheduler) {
        this.file = file;
        this.progressObserver = progressObserver;
        observable = Observable.empty();
        observable.subscribeOn(scheduler).subscribeWith(progressObserver);
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("image/jpeg");
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0;

        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                progressObserver.onNext((int) (100 * uploaded / fileLength));
                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } catch (IOException e) {
            progressObserver.onError(e);
        } finally {
            in.close();
        }
    }
}
