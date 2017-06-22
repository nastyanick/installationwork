package com.nastynick.installationworks.view.camera;

import java.util.List;

public interface CameraBurstView {

    void setTotalImagesCount(int framesCount);

    void setTakenFramesCount(int framesTaken);

    void finishImagesTaking(List<String> frames);
}
