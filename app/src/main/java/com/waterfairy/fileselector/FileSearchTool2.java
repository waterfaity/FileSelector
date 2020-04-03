package com.waterfairy.fileselector;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/6
 * des  : 原作用于搜索图片 ,修改extension  可以搜索指定的文件
 * des  : 如果该文件夹A存在符合条件的文件AB 记录该文件夹A和文件AB ,然后跳过该文件夹A搜索下一个文件夹
 * des  : 未知:file.listFile()  文件夹在前?
 */


public class FileSearchTool2 {
    private static final String TAG = "fileSearchTool";
    private ArrayList<File> fileList = new ArrayList<>();
    private boolean running;
    private OnFileQueryListener onSearchListener;
    private Context applicationContext;

    private FileSearchConfig config;


    private FileSearchTool2(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setConfig(FileSearchConfig config) {
        this.config = config;
    }

    public static FileSearchTool2 newInstance(Context context) {
        return new FileSearchTool2(context);
    }


    public void start() {
        running = true;
        fileList.clear();
        startAsyncTask();
    }

    /**
     * 异步搜索
     */
    private void startAsyncTask() {
        new AsyncTask<Void, File, ArrayList<File>>() {

            @Override
            protected ArrayList<File> doInBackground(Void... voids) {
                //筛选条件
                String select = "";
                String[] ignorePaths = config.getIgnorePaths();
                if (ignorePaths != null && ignorePaths.length != 0) {

                    for (int i = 0; i < ignorePaths.length; i++) {
                        select += (MediaStore.Files.FileColumns.DATA + " not like '" + ignorePaths[i] + "%' and ");
                    }
                    select = "(" + select.substring(0, select.length() - 4) + ")";
                }
                if (config.getExtensions() != null && config.getExtensions().length > 0) {
                    if (!TextUtils.isEmpty(select)) {
                        select = select + " and ";
                    }
                    String temp = "";
                    for (int i = 0; i < config.getExtensions().length; i++) {
                        temp += (MediaStore.Files.FileColumns.DATA + " like '%" + config.getExtensions()[i] + "' or ");
                    }
                    select += "(" + temp.substring(0, temp.length() - 3) + ")";
                }

                //媒体cursor
                if (applicationContext != null) {
                    Cursor cursor = applicationContext.getContentResolver()
                            .query(config.getContentUri(),
                                    config.getProjections(),
                                    select, null,
                                    config.getSortOrder());
                    //添加路径
                    if (cursor.moveToLast()) {
                        do {
                            if (!running) {
                                break;
                            }
                            String[] projections = config.getProjections();
                            String filePath = cursor.getString(cursor.getColumnIndex(projections[0]));
                            if (!TextUtils.isEmpty(filePath)) {
                                File file = new File(filePath);
                                if (file.exists() && file.isFile()) {
                                    //文件存在
                                    publishProgress(file);
                                    fileList.add(file);
                                }
                            }
                        } while (cursor.moveToPrevious());
                    }
                }
                return fileList;
            }

            @Override
            protected void onProgressUpdate(File... values) {
                //搜索某个文件夹
                if (onSearchListener != null) onSearchListener.onSearch(values[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<File> fileArrayList) {
                //搜索完毕
                if (onSearchListener != null) onSearchListener.onSearchSuccess(fileArrayList);
            }
        }.execute();
    }


    public void stop() {
        running = false;
    }


    public void setOnSearchListener(OnFileQueryListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }


    /**
     * 对搜索到的文件夹 排序
     *
     * @param fileList
     */
    private void sortByTime(ArrayList<File> fileList) {
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long x = o1.lastModified();
                long y = o2.lastModified();
                return (x < y) ? 1 : ((x == y) ? 0 : -1);
            }
        });

    }

    public interface OnFileQueryListener {

        void onSearch(File file);

        void onSearchSuccess(ArrayList<File> fileArrayList);
    }


    public void release() {
        onSearchListener = null;
        applicationContext = null;
    }


    public boolean isRunning() {
        return isRunning();
    }
}
