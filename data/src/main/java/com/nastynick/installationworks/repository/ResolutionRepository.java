package com.nastynick.installationworks.repository;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class ResolutionRepository {
    private static final int LOW_RESOLUTION_WIDTH = 800;
    private static final int HIGH_RESOLUTION_WIDTH = 1400;
    private static String WIDTH = "width";

    SharedPreferences sharedPreferences;

    @Inject
    public ResolutionRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int width() {
        return sharedPreferences.getInt(WIDTH, LOW_RESOLUTION_WIDTH);
    }

    public boolean isLow() {
        return LOW_RESOLUTION_WIDTH == width();
    }

    public void setResolution(boolean isLow) {
        sharedPreferences.edit().putInt(WIDTH, isLow ? LOW_RESOLUTION_WIDTH : HIGH_RESOLUTION_WIDTH).apply();
    }

    public void remove() {
        sharedPreferences.edit().remove(WIDTH).apply();
    }
}
