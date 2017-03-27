package com.nastynick.installationworks.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InstallationFileCreator {
    public static File createFile(String fileName, String rootName, String[] directoryNames) {
        File directory = getFileDirectory(Environment.getExternalStorageDirectory(), rootName);
        for (String directoryName : directoryNames) {
            directory = getFileDirectory(directory, directoryName);
        }
        return new File(directory, fileName + ".jpg");
    }

    @NonNull
    private static File getFileDirectory(File parent, String dirName) {
        File directory = new File(parent, dirName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static void saveBitmap(Bitmap bitmap, File file) {
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap bitmapFromUri(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (is != null) {
            is.close();
        }
        return bitmap;
    }
}
