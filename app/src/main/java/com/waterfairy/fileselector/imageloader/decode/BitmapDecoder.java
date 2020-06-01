package com.waterfairy.fileselector.imageloader.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.waterfairy.fileselector.imageloader.ImageRotateTool;

import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/1 16:39
 * @info:
 */
public class BitmapDecoder {

    private final int viewHeight;
    private final String path;
    private final int viewWidth;

    public BitmapDecoder(String path, int viewWidth, int viewHeight) {
        this.path = path;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public Bitmap decode() throws IOException {
        BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(path, false);

        //图片原始宽高
        int srcWidth = bitmapRegionDecoder.getWidth();
        int srcHeight = bitmapRegionDecoder.getHeight();

        //等比缩放
        float minScale = Math.min(srcWidth / (float) viewWidth, srcHeight / (float) viewWidth);

        //裁剪区域额
        Rect rect = new Rect(0, 0, srcWidth, srcHeight);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //计算采样率  (固定为2的N次方) -> 取整数
        //计算指数 double类型  ->  int
        //设置采样率 2的N次方
        options.inSampleSize = (int) minScale;
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //根据配置截取指定区域图片(inSampleSize / inPreferredConfig)
        //未旋转的图像
        Bitmap decodeBitmap = bitmapRegionDecoder.decodeRegion(rect, options);

        Bitmap bitmapResult;
        //旋转
        int rotate = ImageRotateTool.getRotate(path);
        //旋转
        if (rotate != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate, decodeBitmap.getWidth() / 2F, decodeBitmap.getHeight() / 2F);
            bitmapResult = Bitmap.createBitmap(decodeBitmap, 0, 0,
                    decodeBitmap.getWidth(), decodeBitmap.getHeight(), matrix, true);
        } else {
            bitmapResult = decodeBitmap;
        }
        return bitmapResult;
    }
}
