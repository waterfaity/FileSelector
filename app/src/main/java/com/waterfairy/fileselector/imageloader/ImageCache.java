package com.waterfairy.fileselector.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 09:33
 * @info:
 */
public class ImageCache {

    private int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);

    private ImageCache() {
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private LruCache<String, Bitmap> lruCache;
    private static ImageCache imageCache;

    public static ImageCache getInstance() {
        if (imageCache == null) imageCache = new ImageCache();
        return imageCache;
    }

    public void put(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    public Bitmap get(String key) {
        return lruCache.get(key);
    }
}
