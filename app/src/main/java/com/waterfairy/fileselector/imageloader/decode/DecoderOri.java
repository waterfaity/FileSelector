package com.waterfairy.fileselector.imageloader.decode;

import android.os.AsyncTask;
import android.widget.ImageView;

import com.waterfairy.fileselector.imageloader.async.BitmapDecodeOriAsyncTask;

import java.lang.ref.WeakReference;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 19:41
 * @info:
 */
public class DecoderOri extends BitmapDecodeOriAsyncTask {
    private WeakReference<String> path;
    protected WeakReference<String> key;
    private WeakReference<Integer> viewWidth;
    private WeakReference<Integer> viewHeight;
    protected WeakReference<ImageView> imageView;

    public DecoderOri(ImageView imageView, String key, String path, int viewWidth, int viewHeight) {
        this.path = new WeakReference<>(path);
        this.viewWidth = new WeakReference<>(viewWidth);
        this.key = new WeakReference<>(key);
        this.viewHeight = new WeakReference<>(viewHeight);
        this.imageView = new WeakReference<>(imageView);
    }

    public void executeOnExecutor() {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path.get(), viewWidth.get(), viewHeight.get());
    }
}
