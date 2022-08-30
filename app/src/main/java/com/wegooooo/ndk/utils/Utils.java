package com.wegooooo.ndk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <pre>
 *     Copyright (C), 2014-2022, 深圳市微购科技有限公司
 *     Author : tangjianye
 *     Email  : jianye.tang@foxmail.com
 *     Time   : 2022/8/23 16:03
 *     Desc   : 工具类
 *     Version: 1.0
 * </pre>
 */
public class Utils {

    public static Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fullPath(Context context, String filename) {
        return context.getCacheDir() + File.separator + "thumbnails" + File.separator + filename;
    }
}
