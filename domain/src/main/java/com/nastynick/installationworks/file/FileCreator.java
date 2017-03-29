package com.nastynick.installationworks.file;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

public class FileCreator {
    public static File createFile(String fileName, String[] directoryNames) {
        File directory = null;
        for (String directoryName : directoryNames) {
            directory = getFileDirectory(directory == null ? Environment.getExternalStorageDirectory() : directory, directoryName);
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
}
