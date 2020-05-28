package com.waterfairy.fileselector.imageloader.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/28 19:27
 * @info:
 */
public class BitmapSaveAsyncTask extends AsyncTask<Object, Void, Void> {

    @Override
    protected Void doInBackground(Object... objects) {
        File file = (File) objects[0];
        Bitmap bitmap = (Bitmap) objects[1];
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
