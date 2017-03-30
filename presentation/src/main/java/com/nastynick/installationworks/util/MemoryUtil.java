package com.nastynick.installationworks.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class MemoryUtil {
    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    private static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return calculateMegabytes(availableBlocks * blockSize);
    }

    private static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return calculateMegabytes(totalBlocks * blockSize);
    }

    public static long getAvailableMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return calculateMegabytes(availableBlocks * blockSize);
        } else return getAvailableInternalMemorySize();
    }

    public static long getTotalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return calculateMegabytes(totalBlocks * blockSize);
        } else return getTotalInternalMemorySize();
    }

    private static long calculateMegabytes(long size) {
        size /= 1024 * 1024;
        return size;
    }
}
