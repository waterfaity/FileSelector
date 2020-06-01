package com.waterfairy.fileselector.imageloader.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.waterfairy.fileselector.imageloader.decode.BitmapDecoder;
import com.waterfairy.fileselector.imageloader.transform.Transform;

import java.io.IOException;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 14:43
 * @info:
 */
public class BitmapDecodeOriAsyncTask extends AsyncTask<Object, Void, Bitmap> {
    private final String TAG = "DecodeFileAsync";

    @Override
    protected Bitmap doInBackground(Object... objects) {
        if (!isCancelled()) {
            String path = (String) objects[0];
            int viewWidth = (int) objects[1];
            int viewHeight = (int) objects[2];
            List<Transform> transforms = (List<Transform>) objects[3];
//            long time = System.currentTimeMillis();
            try {
                Bitmap decodeBitmap = new BitmapDecoder(path, viewWidth, viewHeight).decode();
                if (transforms != null && decodeBitmap != null) {
                    for (int i = 0; i < transforms.size(); i++) {
                        if (transforms.get(i) != null) {
                            decodeBitmap = transforms.get(i).trans(path, decodeBitmap, viewWidth, viewHeight);
                        }
                    }
                }
//                Log.i(TAG, "doInBackground: " + (System.currentTimeMillis() - time));
                return decodeBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
