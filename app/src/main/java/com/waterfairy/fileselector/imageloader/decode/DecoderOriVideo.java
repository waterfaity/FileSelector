package com.waterfairy.fileselector.imageloader.decode;

import android.os.AsyncTask;
import android.widget.ImageView;

import com.waterfairy.fileselector.imageloader.async.BitmapDecodeOriAsyncTask;
import com.waterfairy.fileselector.imageloader.async.VideoDecodeOriAsyncTask;
import com.waterfairy.fileselector.imageloader.transform.Transform;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 19:41
 * @info:
 */
public class DecoderOriVideo extends VideoDecodeOriAsyncTask {
    private WeakReference<String> path;
    protected WeakReference<String> key;
    private WeakReference<Integer> viewWidth;
    private WeakReference<Integer> viewHeight;
    protected WeakReference<ImageView> imageView;
    protected WeakReference<List<Transform>> transforms;

    public DecoderOriVideo(ImageView imageView, String key, String path, int viewWidth, int viewHeight, List<Transform> transforms) {
        this.path = new WeakReference<>(path);
        this.viewWidth = new WeakReference<>(viewWidth);
        this.key = new WeakReference<>(key);
        this.viewHeight = new WeakReference<>(viewHeight);
        this.imageView = new WeakReference<>(imageView);
        this.transforms = new WeakReference<>(transforms);
    }

    public void executeOnExecutor() {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path.get(), viewWidth.get(), viewHeight.get(), transforms.get());
    }
}
