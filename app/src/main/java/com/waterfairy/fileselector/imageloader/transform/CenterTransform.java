package com.waterfairy.fileselector.imageloader.transform;

import android.graphics.Bitmap;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/1 17:02
 * @info:
 */
public class CenterTransform extends SizeTransForm {

    public static Transform defaultInstance() {
        return new CenterTransform();
    }

    /**
     * @param path           本地原始路径
     * @param compressBitmap 压缩后的bitmap
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    @Override
    public Bitmap trans(String path, Bitmap compressBitmap, int viewWidth, int viewHeight) {
        width = viewWidth;
        height = viewHeight;
        return super.trans(path, compressBitmap, viewWidth, viewHeight);
    }

    @Override
    public String getKey() {
        return "CenterTransform";
    }
}
