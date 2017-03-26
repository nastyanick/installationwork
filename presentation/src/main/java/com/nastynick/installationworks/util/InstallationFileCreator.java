package com.nastynick.installationworks.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;

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
}
