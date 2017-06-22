package com.nastynick.installationworks.file;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Helper to manage files and directories
 */
public class FileManager {
    public static File createFile(String fileName, String[] directoryNames, String extension) {
        File directory = null;
        for (String directoryName : directoryNames) {
            directory = getFileDirectory(directory == null ? Environment.getExternalStorageDirectory() : directory, directoryName);
        }
        return new File(directory, fileName + "." + extension);
    }

    @NonNull
    private static File getFileDirectory(File parent, String dirName) {
        File directory = new File(parent, dirName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static boolean clear(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return path.delete();
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    clear(file);
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }
}
