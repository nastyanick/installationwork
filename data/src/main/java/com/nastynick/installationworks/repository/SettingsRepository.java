package com.nastynick.installationworks.repository;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Class SettingsRepository provides access to photo's resolution storage
 */
public class SettingsRepository {
    private static final int LOW_RESOLUTION_WIDTH = 800;
    private static final int HIGH_RESOLUTION_WIDTH = 1400;
    private static final int MIN_GIF_FRAMES_COUNT = 3;
    private static final int DEFAULT_GIF_FRAMES_DELAY = 500;

    private static final String WIDTH = "width";
    private static final String GIF_TURNED = "gif_turned";
    private static final String GIF_FRAMES_COUNT = "gif_frames_count";
    private static final String GIF_FRAMES_DELAY = "gif_frames_delay";

    private SharedPreferences sharedPreferences;

    @Inject
    public SettingsRepository(SharedPreferences sharedPreferences) {
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

    public int getGifWidth() {
        return width();
    }

    public int getFramesDelay() {
        return sharedPreferences.getInt(GIF_FRAMES_DELAY, DEFAULT_GIF_FRAMES_DELAY);
    }

    public void setFramesDelay(int delay) {
        sharedPreferences.edit().putInt(GIF_FRAMES_DELAY, delay).apply();
    }
}
