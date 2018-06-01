package com.waterfairy.fileselector;

import android.app.Application;

import com.waterfairy.utils.ToastUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/1 11:14
 * @info:
 */
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.initToast(this);
    }

}
