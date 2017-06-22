package com.nastynick.installationworks.interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nastynick.installationworks.file.FileManager;
import com.nastynick.installationworks.gifmaker.AnimatedGifEncoder;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;

public class GifCreating extends UseCase {

    public static final int GIF_QUALITY = 20;
    private static final int GIF_FRAMES_DELAY = 500;

    @Inject
    public GifCreating() {
    }

    public void makeGif(File gifFile, String[] files, Observer<File> gifObserver) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        gifEncoder.start(baos);//start
        gifEncoder.setRepeat(0);
        gifEncoder.setDelay(GIF_FRAMES_DELAY);
        gifEncoder.setQuality(GIF_QUALITY);

        Observable.fromArray(files)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(file -> Observable.just(file)
                        .map(fileName -> {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.RGB_565;
                            return BitmapFactory.decodeFile(fileName, options);
                        })
                        .doOnNext(gifEncoder::addFrame))
                .toList()
                .subscribe(res -> writeGif(gifFile, gifEncoder, baos, gifObserver));
    }


    private void writeGif(File gifFile, AnimatedGifEncoder gifEncoder, ByteArrayOutputStream baos, Observer<File> gifObserver) throws IOException {
        gifEncoder.finish();//finish

        FileOutputStream fos = new FileOutputStream(gifFile);
        baos.writeTo(fos);
        baos.flush();
        fos.flush();
        baos.close();
        fos.close();

        gifObserver.onNext(gifFile);
    }

    public String createPictureFile(byte[] picture, String root) throws IOException {
        File file = FileManager.createFile(String.valueOf(System.currentTimeMillis()), new String[]{root, "temp"}, ProcessFileUseCase.JPG_EXTENSION);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(picture);
        bos.flush();
        bos.close();
        return file.getPath();
    }
}
