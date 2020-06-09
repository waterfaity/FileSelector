package com.waterfairy.fileselector.imageloader.transform;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/1 16:21
 * @info:
 */
public class SizeTransForm implements Transform {

    protected int width;
    protected int height;

    /**
     * 默认是view的宽高
     */
    public SizeTransForm() {
    }

    public SizeTransForm(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public SizeTransForm setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public SizeTransForm setHeight(int height) {
        this.height = height;
        return this;
    }


    /**
     * @param path           本地原始路径
     * @param compressBitmap 压缩后的bitmap
     * @return
     */
    @Override
    public Bitmap trans(String path, Bitmap compressBitmap, int viewWidth, int viewHeight) {

        if (width == 0 || height == 0) {
            width = viewWidth;
            height = viewHeight;
        }
        int bitmapWidth = compressBitmap.getWidth();
        int bitmapHeight = compressBitmap.getHeight();

        int targetWidth = 0;
        int targetHeight = 0;

        //x/y起点计算
        int dx = 0;//x起点
        int dy = 0;//y起点
        if ((width / (float) height > bitmapWidth / (float) bitmapHeight)) {
            //view 更宽
            targetWidth = bitmapWidth;
            targetHeight = (int) (bitmapWidth * height / (float) width);
            dy = (bitmapHeight - targetHeight) / 2;
        } else {
            targetWidth = (int) (bitmapHeight * width / (float) height);
            targetHeight = bitmapHeight;
            dx = (bitmapWidth - targetWidth) / 2;
        }
        Bitmap bitmap = Bitmap.createBitmap(compressBitmap, dx, dy, targetWidth, targetHeight);
        Bitmap tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, width, height), getPaint());
        return tempBitmap;
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        return paint;
    }

    /**
     * Transform唯一表示
     *
     * @return
     */
    @Override
    public String getKey() {
        return "SizeTransForm -> " + width + ":" + height;
    }
}
