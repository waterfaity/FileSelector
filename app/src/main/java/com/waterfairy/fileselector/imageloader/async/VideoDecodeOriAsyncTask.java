package com.waterfairy.fileselector.imageloader.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.waterfairy.fileselector.imageloader.decode.VideoDecodeUtils;
import com.waterfairy.fileselector.imageloader.transform.Transform;

import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 14:43
 * @info:
 */
public class VideoDecodeOriAsyncTask extends AsyncTask<Object, Void, Bitmap> {
    private final String TAG = "DecodeFileAsync";

    @Override
    protected Bitmap doInBackground(Object... objects) {
        if (!isCancelled()) {
            String path = (String) objects[0];
            int viewWidth = (int) objects[1];
            int viewHeight = (int) objects[2];
            List<Transform> transforms = (List<Transform>) objects[3];
            long time = System.currentTimeMillis();
            Bitmap decodeBitmap = VideoDecodeUtils.createVideoThumbnail(path, viewWidth, viewHeight);
            if (decodeBitmap != null) {
                if (transforms != null) {
                    for (int i = 0; i < transforms.size(); i++) {
                        if (transforms.get(i) != null) {
                            decodeBitmap = transforms.get(i).trans(path, decodeBitmap, viewWidth, viewHeight);
                        }
                    }
                }
            }
//            Log.i(TAG, "doInBackground: " + (System.currentTimeMillis() - time));
            return decodeBitmap;
        }
        return null;
    }
}
