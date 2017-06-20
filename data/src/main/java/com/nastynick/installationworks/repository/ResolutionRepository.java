package com.nastynick.installationworks.repository;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Class ResolutionRepository provides access to photo's resolution storage
 */
public class ResolutionRepository {
    private static final int LOW_RESOLUTION_WIDTH = 800;
    private static final int HIGH_RESOLUTION_WIDTH = 1400;
    private static final int MIN_GIF_FRAMES_COUNT = 3;

    private static final String WIDTH = "width";
    private static final String GIF_TURNED = "gif_turned";
    private static final String GIF_FRAMES_COUNT = "gif_frames_count";

    private SharedPreferences sharedPreferences;

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

    public boolean isGifTurned() {
        return sharedPreferences.getBoolean(GIF_TURNED, false);
    }

    public void setGifTurned(boolean turned) {
        sharedPreferences.edit().putBoolean(GIF_TURNED, turned).apply();
    }

    public void setGifFramesCount(int value) {
        sharedPreferences.edit().putInt(GIF_FRAMES_COUNT, value).apply();
    }

    public int getGifGFramesCount() {
        return sharedPreferences.getInt(GIF_FRAMES_COUNT, MIN_GIF_FRAMES_COUNT);
    }
}
