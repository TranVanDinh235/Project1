package com.example.project1.util;

import android.os.Handler;

public interface CameraHelper {
    void openCamera();
    void closeCamera();
    void setupCamera(int width, int height);
    void takeImage();
    void setTextureViewListener();
    void setHandler(Handler handler);
}
