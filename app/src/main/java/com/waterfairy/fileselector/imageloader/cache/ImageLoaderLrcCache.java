package com.waterfairy.fileselector.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 09:33
 * @info: 图片缓存器(缓存到内存)
 */
public class ImageLoaderLrcCache {

    /**
     * 最大可用内存的1/8
     */
    private int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);

    private ImageLoaderLrcCache() {
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private LruCache<String, Bitmap> lruCache;
    private static ImageLoaderLrcCache imageCache;

    public static ImageLoaderLrcCache getInstance() {
        if (imageCache == null) imageCache = new ImageLoaderLrcCache();
        return imageCache;
    }

    public void put(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    public Bitmap get(String key) {
        return lruCache.get(key);
    }
}
