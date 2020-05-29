package com.waterfairy.fileselector.imageloader.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.waterfairy.fileselector.imageloader.ImageRotateTool;

import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 14:43
 * @info:
 */
public class BitmapDecodeOriAsyncTask extends AsyncTask<Object, Void, Bitmap> {
    private Paint paint;
    private final String TAG = "DecodeFileAsync";

    @Override
    protected Bitmap doInBackground(Object... objects) {

        if (!isCancelled()) {
            String path = (String) objects[0];
            int viewWidth = (int) objects[1];
            int viewHeight = (int) objects[2];

            BitmapRegionDecoder bitmapRegionDecoder = null;
            try {
                logTime(null);

                bitmapRegionDecoder = BitmapRegionDecoder.newInstance(path, false);
                //图片原始宽高
                int srcWidth = bitmapRegionDecoder.getWidth();
                int srcHeight = bitmapRegionDecoder.getHeight();
                //获取居中最大裁剪后的宽高  (viewWidth/viewHeight 比例的宽高)

                //旋转
                int rotate = ImageRotateTool.getRotate(path);
                //是否有旋转
                boolean hasRotate = rotate == 90 || rotate == 270;


                int tempSrcWidth = hasRotate ? srcHeight : srcWidth;
                int tempSrcHeight = hasRotate ? srcWidth : srcHeight;

                int targetWidth = 0;
                int targetHeight = 0;


                //x/y起点计算
                int dx = 0;//x起点
                int dy = 0;//y起点
                if ((viewWidth / (float) viewHeight > tempSrcWidth / (float) tempSrcHeight)) {
                    //view 更宽
                    targetWidth = tempSrcWidth;
                    targetHeight = (int) (tempSrcWidth * viewHeight / (float) viewWidth);

                    dy = (tempSrcHeight - targetHeight) / 2;
                } else {
                    targetWidth = (int) (tempSrcHeight * viewWidth / (float) viewHeight);
                    targetHeight = tempSrcHeight;

                    dx = (tempSrcWidth - targetWidth) / 2;
                }

                if (hasRotate) {
                    int temp = dx;
                    dx = dy;
                    dy = temp;

                    temp = targetWidth;
                    targetWidth = targetHeight;
                    targetHeight = temp;
                }

                //裁剪区域额
                Rect rect = new Rect(dx, dy, targetWidth + dx, targetHeight + dy);
                BitmapFactory.Options options = new BitmapFactory.Options();
                //计算采样率  (固定为2的N次方) -> 取整数
                //计算指数 double类型  ->  int
                //设置采样率 2的N次方
                options.inSampleSize = (int) (targetWidth / (float) viewWidth);
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                //根据配置截取指定区域图片(inSampleSize / inPreferredConfig)
                //未旋转的图像
                Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);

                Bitmap bitmapResult;
                //旋转
                if (rotate != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate, bitmap.getWidth() / 2F, bitmap.getHeight() / 2F);
                    bitmapResult = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                } else {
                    bitmapResult = bitmap;
                }

                //bitmapResult 旋转过的图像

                //宽高
                if (bitmapResult.getWidth() != viewWidth) {
                    //不是指定宽高  缩放处理
                    Bitmap temp = Bitmap.createBitmap(viewWidth, viewHeight, bitmap.getConfig());
                    Canvas canvas = new Canvas(temp);
                    canvas.drawBitmap(bitmapResult,
                            new Rect(0, 0, bitmapResult.getWidth(), bitmapResult.getHeight()),
                            new Rect(0, 0, viewWidth, viewHeight), getPaint());
                    bitmapResult = temp;
                }
                return bitmapResult;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    long currentTime = 0;

    void logTime(String title) {

        if (currentTime == 0) {
            currentTime = System.currentTimeMillis();
        } else {
            if (TextUtils.isEmpty(title)) return;
            Log.i("logTime", title + ": " + (System.currentTimeMillis() - currentTime));
            currentTime = System.currentTimeMillis();
        }
    }

    private Paint getPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
        }
        return paint;
    }
}
