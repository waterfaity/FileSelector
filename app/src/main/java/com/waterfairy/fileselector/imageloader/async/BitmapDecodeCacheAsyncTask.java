package com.waterfairy.fileselector.imageloader.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 14:43
 * @info:
 */
public class BitmapDecodeCacheAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private final String TAG = "DecodeFileAsync";

    @Override
    protected Bitmap doInBackground(String... objects) {
        if (!isCancelled()) {
            String path = objects[0];
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }
}
