package com.wegooooo.ndk.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 *     Copyright (C), 2014-2022, 深圳市微购科技有限公司
 *     Author : Administrator
 *     Email  : jianye.tang@foxmail.com
 *     Time   : 2022/8/11 14:54
 *     Desc   : 工具类
 *     Version: 1.0
 * </pre>
 */
public class MnnUtils {
    private static final String TAG = "wegomnn";

    public static void copyModelFromAssetsToData(Context context) {
        // assets目录的下模型文件名
        String[] models = {
                "wg_cbir_model.mnn",
                "cat.jpg"
        };

        Log.i(TAG, "mnn init");
        try {
            for (String model : models) {
                copyAssetFileToFiles(context, model);
            }
            Log.i(TAG, "Copy model Success");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Copy model Error");
        }
    }

    public static void copyAssetDirToFiles(Context context, String dirname) throws IOException {
        File dir = new File(context.getFilesDir() + File.separator + dirname);
        dir.mkdir();

        AssetManager assetManager = context.getAssets();
        String[] children = assetManager.list(dirname);
        for (String child : children) {
            child = dirname + File.separator + child;
            String[] grandChildren = assetManager.list(child);
            if (0 == grandChildren.length) {
                copyAssetFileToFiles(context, child);
            } else {
                copyAssetDirToFiles(context, child);
            }
        }
    }

    public static void copyAssetFileToFiles(Context context, String filename) throws IOException {
        InputStream is = context.getAssets().open(filename);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();

        File of = new File(context.getFilesDir() + File.separator + filename);
        of.createNewFile();
        FileOutputStream os = new FileOutputStream(of);
        os.write(buffer);
        os.close();
    }
}
