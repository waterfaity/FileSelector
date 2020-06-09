package com.waterfairy.fileselector.imageloader.tool;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/1 18:26
 * @info:
 */
public class VideoThumbTool {

    public static Bitmap createVideoThumbnail(String url, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(url);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float minSize = Math.min(width / (float) maxWidth, height / (float) maxHeight);
            if (minSize > 1) {
                minSize = 1 / minSize;
                Matrix matrix = new Matrix();
                matrix.postScale(minSize, minSize, width / 2, height / 2);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            }
        }
        return bitmap;
    }
}
