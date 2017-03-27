package com.nastynick.installationworks.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class WaterMarker {
    public static Bitmap mark(Bitmap src, String watermark) {
        int width = src.getWidth() / 2;
        int height = src.getHeight() / 2;

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

//    private static Bitmap getBitmap(Context context, Bitmap src, int width, int height) {
//        Bitmap bm = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 5;
//        AssetFileDescriptor fileDescriptor = null;
//        try {
//            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(src, "");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
//                fileDescriptor.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
}
