package com.waterfairy.fileselector.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.waterfairy.fileselector.imageloader.async.BitmapSaveAsyncTask;

import java.io.File;
import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 19:27
 * @info: 缓存到本地
 */
public class ImageLocalCache {
    public static String cachePath = "";
    //默认(不设置  cachePath)  externalCacheDir + "/" + cachePathChildName
    private static String cachePathChildName = "imageLoader";

    /**
     * 本地缓存是否存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isCacheExist(Context context, String key) {
        File imageFile = getSaveFilePath(context, key);
        return imageFile != null && imageFile.exists() && imageFile.canRead();
    }

    /**
     * 获取保存的文件
     *
     * @param context
     * @param key
     * @return
     */
    public static File getSaveFilePath(Context context, String key) {
        File imageFile = null;
        if (context != null) {
            if (TextUtils.isEmpty(cachePath)) {
                File externalCacheDir = context.getExternalCacheDir();
                if (externalCacheDir != null) {
                    imageFile = new File(externalCacheDir + "/" + cachePathChildName, key);
                }
            } else {
                imageFile = new File(cachePath, key);
            }
        }
        return imageFile;
    }

    /**
     * 缓存到本地
     *
     * @param context
     * @param key     全路径:  cachePath / cachePathChildName / key
     * @param bitmap
     */
    public static void saveCache(Context context, String key, Bitmap bitmap) {
        boolean fileExist = false;
        File imageFile = getSaveFilePath(context, key);
        if (imageFile != null && bitmap != null && !bitmap.isRecycled()) {
            if (!imageFile.exists()) {
                File parentFile = imageFile.getParentFile();
                if (parentFile != null) {

                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    if (parentFile.exists()) {
                        try {
                            imageFile.createNewFile();
                            fileExist = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                fileExist = true;
            }
        }
        if (fileExist) {
            new BitmapSaveAsyncTask().execute(imageFile, bitmap);
        }
    }
}
