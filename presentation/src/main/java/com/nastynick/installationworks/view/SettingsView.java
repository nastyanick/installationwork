package com.nastynick.installationworks.view;

public interface SettingsView {
    void setSelected(boolean lowSelected);

    void setSettingsAccount(String login);

    void setGifTurned(boolean turned);

    void setGifFramesCount(int gifFramesCount);

    void setGifFramesDelay(int delay);

    void setFramesPickerVisibility(boolean visibile);
}
