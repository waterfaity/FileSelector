package com.waterfairy.fileselector.imageloader.tool;

import android.media.ExifInterface;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-11-12 17:29
 * @info:
 */
public class ImageRotateTool {


    /**
     * 获取旋转角度
     *
     * @param imgPath
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public static int getRotate(@NonNull String imgPath) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imgPath);
        int anInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (anInt) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }

}
