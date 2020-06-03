package com.waterfairy.fileselector.imageloader.listener;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/3 10:01
 * @info:
 */
public interface OnLoadListener {

    void onLoadSuccess(String path, ImageView imageView, Bitmap bitmap);

}
