package com.nastynick.installationworks.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import io.reactivex.Observer;

import static android.graphics.Bitmap.CompressFormat.PNG;

public class WaterMarker {
    private static Bitmap mark(Bitmap src, String watermark) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

        Paint waterMarkBackground = new Paint();
        waterMarkBackground.setColor(Color.parseColor("#AA000000"));
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        int padding = (int) (height * 0.05);

        Rect textBounds = setTextSize(paint, watermark, width - padding * 2);
        int top = height - textBounds.height() - padding * 2;
        canvas.drawRect(0, top, width, height, waterMarkBackground);
        canvas.drawText(watermark, padding, height - padding, paint);

        return result;
    }

    private static Rect setTextSize(Paint paint, String text, int desiredWidth) {
        int textSize = 0;
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() < desiredWidth) {
            textSize++;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }
        return bounds;
    }

    public static void resizeImage(File file, int maxResolution, String waterMark, Observer observer) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        final String imageType = options.outMimeType;

        int targetWidth = options.outWidth;
        int targetHeight = options.outHeight;

        ImageSize targetSize = new ImageSize(targetWidth, targetHeight);

        int maxSize = Math.max(targetWidth, targetHeight);
        if (maxSize > maxResolution) {
            float scale = (float) maxResolution / (float) maxSize;
            targetSize = targetSize.scale(scale);
        }

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();

        ImageLoader.getInstance().loadImage("file://" + file.getAbsolutePath(), targetSize, displayImageOptions,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Bitmap markedImage = mark(loadedImage, waterMark);
                        writeBitmapToFile(markedImage, file, imageType);
                        observer.onComplete();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        observer.onError(failReason.getCause());
                    }
                });
    }

    protected static File writeBitmapToFile(Bitmap bitmap, File file, String imageType) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(PNG.equals(imageType) ? PNG : Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (FileNotFoundException e) {
            Log.i("gallery", e.getMessage());
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return null;
    }
}
