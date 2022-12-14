package com.wegooooo.ndk;

import android.app.Application;
import android.util.Log;

import com.wegooooo.mnn.WegoMnn;

/**
 * <pre>
 *     Copyright (C), 2014-2022, 深圳市微购科技有限公司
 *     Author : Administrator
 *     Email  : jianye.tang@foxmail.com
 *     Time   : 2022/8/11 15:05
 *     Desc   : Mnn 应用
 *     Version: 1.0
 * </pre>
 */
public class MnnApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WegoMnn.initialization(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("wegomnn", "内存不足！");
    }
}
