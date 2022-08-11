package com.wegooooo.ndk;

import android.app.Application;

import com.wegooooo.ndk.utils.MnnUtils;

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
        MnnUtils.copyModelFromAssetsToData(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
