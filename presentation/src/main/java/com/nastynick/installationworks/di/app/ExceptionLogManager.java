package com.nastynick.installationworks.di.app;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class ExceptionLogManager writes exceptions to file
 */
public class ExceptionLogManager {

    private static final String FILE_EXCEPTIONS = "installation_work_exceptions.txt";
    private static final String ROOT_DIRECTORY = "installation_work";
    protected Context context;

    public ExceptionLogManager(Context context) {
        this.context = context;
    }

    public void addException(Throwable exception) {
        Crashlytics.logException(exception);

        File file = getExceptionsFile();
        saveException(file, exception.getMessage() + Arrays.toString(exception.getStackTrace()));
    }

    public File getExceptionsFile() {
        File root = new File(context.getExternalCacheDir(), ROOT_DIRECTORY);
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, FILE_EXCEPTIONS);
        try {
            file.createNewFile();
        } catch (IOException e) {
            Crashlytics.logException(e);
        }
        return file;
    }

    private void saveException(File file, String exception) {
        FileWriter writer;
        try {
            writer = new FileWriter(file, true);
            writer.append(exception).append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Crashlytics.logException(e);
        }
    }

}
