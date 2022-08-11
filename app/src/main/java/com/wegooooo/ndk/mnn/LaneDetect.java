package com.wegooooo.ndk.mnn;

import android.graphics.Bitmap;

public class LaneDetect {

    static {
        System.loadLibrary("wegomnn"); // tfj,调用生成的库
    }

    public static native LaneInfo[] mnn(String imagePath, String mnnPath);
    public static native void init(String name, String path, boolean useGPU);
    public static native LaneInfo[] detect(Bitmap bitmap, byte[] imageBytes, int width, int height, double threshold, double lens_threshold);
}
