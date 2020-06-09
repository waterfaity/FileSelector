package com.waterfairy.fileselector.imageloader.transform;

import android.graphics.Bitmap;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/1 16:21
 * @info:
 */
public interface Transform {
    /**
     * @param path           本地原始路径
     * @param compressBitmap 压缩后的bitmap
     * @return
     */
    Bitmap trans(String path, Bitmap compressBitmap, int viewWidth, int viewHeight);

    /**
     * Transform 唯一表示
     *
     * @return
     */
    String getKey();
}
