package com.waterfairy.fileselector.imageloader.reference;

import android.os.AsyncTask;
import android.widget.ImageView;

import com.waterfairy.fileselector.imageloader.async.BitmapDecodeCacheAsyncTask;

import java.lang.ref.WeakReference;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 19:41
 * @info:
 */
public class DecoderCache extends BitmapDecodeCacheAsyncTask {
    private WeakReference<String> cachePath;
    protected WeakReference<String> key;
    protected WeakReference<ImageView> imageView;

    public DecoderCache(ImageView imageView, String key, String cachePath) {
        this.cachePath = new WeakReference<>(cachePath);
        this.key = new WeakReference<>(key);
        this.imageView = new WeakReference<>(imageView);
    }

    public void executeOnExecutor() {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cachePath.get());
    }
}
