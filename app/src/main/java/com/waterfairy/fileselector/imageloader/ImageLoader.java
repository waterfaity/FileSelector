package com.waterfairy.fileselector.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.waterfairy.fileselector.imageloader.decode.DecoderCache;
import com.waterfairy.fileselector.imageloader.decode.DecoderOri;

import java.io.File;
import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 09:31
 * @info: 本地文件加载
 */
public class ImageLoader {

    private static final int IMAGE_LOADER_KEY = 2020052812;
    private static final int IMAGE_DECODER_ORI_TASK = 2020052813;
    private static final int IMAGE_DECODER_CACHE_TASK = 2020052814;

    private static final String TAG = "ImageLoader";
    private Context context;
    private String path;
    private ImageView imageView;

    private ImageLoader(Context context) {
        this.context = context;
    }

    public static ImageLoader with(Context context) {
        return new ImageLoader(context);
    }

    public ImageLoader load(String path) {
        this.path = path;
        return this;
    }

    public ImageLoader load(File file) {
        this.path = file.getAbsolutePath();
        return this;
    }

    public void into(final ImageView imageView) {
        this.imageView = imageView;
        if (imageView != null) {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    if (file.canRead()) {
                        imageView.setImageBitmap(null);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                onSize();
                            }
                        });
                    } else {
                        new Exception("ImageLoader:file has no write permission !").printStackTrace();
                    }
                } else {
                    new Exception("ImageLoader:file is not exist !").printStackTrace();
                }
            } else {
                new Exception("ImageLoader:path = null !").printStackTrace();
            }
        } else {
            new Exception("ImageLoader:imageView = null !").printStackTrace();
        }
    }

    private void onSize() {
        String key = MD5Utils.getMD5Code(path + ":" + imageView.getWidth() + "-" + imageView.getHeight());
        imageView.setTag(IMAGE_LOADER_KEY, key);
        Bitmap bitmap = ImageCache.getInstance().get(key);
        if (bitmap == null || bitmap.isRecycled()) {
            imageView.setImageBitmap(null);
            //本地获取
            boolean cacheExist = ImageCacheLocal.isCacheExist(context, key);
            if (!cacheExist) {
                //原图获取
                decodeOri(path, key, imageView.getWidth(), imageView.getHeight());
            } else {
                //本地缓存
                decodeCache(key);
            }
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 从本地缓存获取
     *
     * @param path
     * @param key
     */
    private void decodeCache(String key) {
        Object tag = imageView.getTag(IMAGE_DECODER_CACHE_TASK);
        if (tag instanceof DecoderCache && !((DecoderCache) tag).isCancelled()) {
            ((DecoderCache) tag).cancel(true);
        }
        File cacheFile;
        if ((cacheFile = ImageCacheLocal.getSaveFilePath(context, key)) != null) {
            DecoderCache decoder = new DecoderCache(imageView, key, cacheFile.getAbsolutePath()) {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null && key != null && key.get() != null) {
                        ImageCache.getInstance().put(key.get(), bitmap);
                        ImageView imageView = this.imageView.get();
                        if (imageView != null && key.get().equals(imageView.getTag(IMAGE_LOADER_KEY))) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }
            };
            imageView.setTag(IMAGE_DECODER_CACHE_TASK, decoder);
            decoder.executeOnExecutor();
        }

    }


    /**
     * 获取原始图片
     *
     * @param key        缓存
     * @param viewWidth  view 宽
     * @param viewHeight view 高
     * @throws IOException
     */
    private void decodeOri(String path, String key, int viewWidth, int viewHeight) {

        Object tag = imageView.getTag(IMAGE_DECODER_ORI_TASK);
        if (tag instanceof DecoderOri && !((DecoderOri) tag).isCancelled()) {
            ((DecoderOri) tag).cancel(true);
        }
        DecoderOri decoder = new DecoderOri(imageView, key, path, viewWidth, viewHeight) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null && key != null && key.get() != null) {
                    ImageCache.getInstance().put(key.get(), bitmap);
                    ImageView imageView = this.imageView.get();
                    if (imageView != null && key.get().equals(imageView.getTag(IMAGE_LOADER_KEY))) {
                        imageView.setImageBitmap(bitmap);
                        ImageCacheLocal.saveCache(context, key.get(), bitmap);
                    }
                }
            }
        };
        imageView.setTag(IMAGE_DECODER_ORI_TASK, decoder);
        decoder.executeOnExecutor();
    }
}
