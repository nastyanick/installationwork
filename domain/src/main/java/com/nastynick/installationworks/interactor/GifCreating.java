package com.nastynick.installationworks.interactor;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.nastynick.installationworks.gifmaker.AnimatedGifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GifCreating extends UseCase {

    @Inject
    public GifCreating() {
    }


    public void makeGif(final Bitmap bitmap) {
        Single.just(bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Bitmap, Boolean>() {
                    @Override
                    public Boolean apply(Bitmap bitmap) throws Exception {
                        return createGif(bitmap);
                    }
                })
                .subscribe(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean value) {
                        Log.i("GifCreatingSuccess", String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("GifCreatingError", e.getMessage());
                    }
                });
    }

    private boolean createGif(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);//start
        localAnimatedGifEncoder.setRepeat(0);
        localAnimatedGifEncoder.setDelay(500);
        localAnimatedGifEncoder.setQuality(20);

//        if (pics.isEmpty()) {
//        localAnimatedGifEncoder.addFrame(BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_1));
        localAnimatedGifEncoder.addFrame(bitmap);
        localAnimatedGifEncoder.addFrame(bitmap);
        localAnimatedGifEncoder.addFrame(bitmap);
        localAnimatedGifEncoder.addFrame(bitmap);
//        } else {
//            for (int i = 0; i < pics.size(); i++) {
//                 Bitmap localBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(pics.get(i)), 512, 512);
//                localAnimatedGifEncoder.addFrame(BitmapFactory.decodeFile(pics.get(i)));
//            }
//        }
        localAnimatedGifEncoder.finish();//finish

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFMakerDemo");
        if (!file.exists()) file.mkdir();
        final String path = Environment.getExternalStorageDirectory().getPath() + "/GIFMakerDemo/" + "gifmaker.gif";

        FileOutputStream fos = new FileOutputStream(path);
        baos.writeTo(fos);
        baos.flush();
        fos.flush();
        baos.close();
        fos.close();
        return true;
    }
}
