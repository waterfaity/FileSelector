package com.waterfairy.fileselector.search;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private ArrayList<FolderSearchBean> fileList = new ArrayList<>();
    private boolean running;
    private OnSearchListener onSearchListener;
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
        new AsyncTask<Void, String, ArrayList<FolderSearchBean>>() {

            @Override
            protected ArrayList<FolderSearchBean> doInBackground(Void... voids) {


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
                    for (int i = 0; i < config.getExtensions().length; i++) {
                        select += (MediaStore.Files.FileColumns.DATA + " like '%" + config.getExtensions()[i] + "' or ");
                    }
                    select = "(" + select.substring(0, select.length() - 3) + ")";
                }

                Log.i("test", "doInBackground: " + select);
                //媒体cursor
                if (applicationContext != null) {
                    Cursor cursor = applicationContext.getContentResolver()
                            .query(config.getContentUri(),
                                    config.getProjections(),
                                    select, null,
                                    config.getSortOrder());
                    //添加路径
                    ArrayList<String> tempImageParentPathList = new ArrayList<>();
                    ArrayList<FileSearchBean> allImageList = new ArrayList<>();
                    if (cursor.moveToLast()) {
                        long time = 0;
                        Log.i(TAG, "doInBackground: " + (time = Calendar.getInstance().getTimeInMillis()));
                        do {
                            String[] projections = config.getProjections();
                            String image = cursor.getString(cursor.getColumnIndex(projections[0]));
                            if (!TextUtils.isEmpty(image)) {
                                File file = new File(image);
                                String parentPath = file.getParent();
                                if (new File(image).exists()) {
                                    //文件存在
                                    if (!tempImageParentPathList.contains(parentPath)) {
                                        publishProgress(parentPath);
                                        //文件合并
                                        //文件件已经添加
                                        tempImageParentPathList.add(file.getParent());
                                        FolderSearchBean searchFolderBean = new FolderSearchBean(parentPath, file.getAbsolutePath());
                                        fileList.add(searchFolderBean);
                                    }
                                    //文件合并
                                    allImageList.add(new FileSearchBean(image));
                                }
                            }
                        } while (cursor.moveToPrevious());
                        Log.i(TAG, "doInBackground: " + (Calendar.getInstance().getTimeInMillis() - time));
                    }
                    if (config.isMerge()) {
                        //合并为全部图片
                        FolderSearchBean searchImgBeanAll = new FolderSearchBean();
                        searchImgBeanAll.setIsAll(true);
                        searchImgBeanAll.addChildImageBeans(allImageList);
                        if (allImageList.size() != 0) {
                            searchImgBeanAll.setFirstImgPath(allImageList.get(0).getPath());
                        }
                        fileList.add(0, searchImgBeanAll);
                    }
                }

                return fileList;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                //搜索某个文件夹
                if (onSearchListener != null) onSearchListener.onSearch(values[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<FolderSearchBean> strings) {
                //搜索完毕
                if (onSearchListener != null) onSearchListener.onSearchSuccess(strings);
            }
        }.

                execute();
    }

    /**
     * 移出指定文件夹
     */
    private void removeSpePaths() {
        if (config.getIgnorePaths() != null && config.getIgnorePaths().length > 0) {
            if (fileList != null && fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    FolderSearchBean searchFolderBean = fileList.get(i);
                    for (int j = 0; j < config.getIgnorePaths().length; j++) {
                        if (TextUtils.equals(searchFolderBean.getPath(), config.getIgnorePaths()[i])) {
                            fileList.remove(searchFolderBean);
                            i--;
                        }
                    }
                }
            }
        }
    }


    public void stop() {
        running = false;
    }


    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    /**
     * 搜索指定文件夹Bean 获取相应资源
     * 有排序并且设置第一张图片  和图片的总数量
     *
     * @param folderBean
     * @return
     */
    public List<FileSearchBean> searchFolder(FolderSearchBean folderBean) {
        List<FileSearchBean> imgList = null;
        if (folderBean.isAll()) {
            //搜索完展示的时候会调用该方法
            imgList = folderBean.getChildImgBeans();
        } else {
            //搜索合并所有数据时以及展示时候调用
            imgList = searchFolder(folderBean.getPath());
        }
        //排序
        sortByTime(imgList);
        if (imgList != null && imgList.size() > 0) {
            folderBean.setFirstImgPath(imgList.get(0).getPath());
            folderBean.setNum(imgList.size());
        }
        return imgList;
    }

    /**
     * 指定文件夹路径搜索
     *
     * @param path
     * @return
     */
    public List<FileSearchBean> searchFolder(String path) {
        List<FileSearchBean> fileSearchBeans = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    String childPath = file.getAbsolutePath();
                    for (String anExtension : config.getExtensions()) {
                        if (childPath.endsWith(anExtension)) {
                            fileSearchBeans.add(new FileSearchBean(childPath));
                        }
                    }
                }
            }
        }

        return fileSearchBeans;
    }

    /**
     * 对搜索到的文件夹下的文件时间排序
     *
     * @param imgBeans
     */
    public void sortByTime(List<FileSearchBean> imgBeans) {
        if (imgBeans == null || imgBeans.size() == 0) return;
        Collections.sort(imgBeans, new Comparator<FileSearchBean>() {
            @Override
            public int compare(FileSearchBean o1, FileSearchBean o2) {
                long x = new File(o1.getPath()).lastModified();
                long y = new File(o2.getPath()).lastModified();
                return (x < y) ? 1 : ((x == y) ? 0 : -1);
            }
        });
    }


    /**
     * 对搜索到的文件夹 排序
     *
     * @param fileList
     */
    private void sortByTime(ArrayList<FolderSearchBean> fileList) {
        Collections.sort(fileList, new Comparator<FolderSearchBean>() {
            @Override
            public int compare(FolderSearchBean o1, FolderSearchBean o2) {
                long x = new File(o1.getPath()).lastModified();
                long y = new File(o2.getPath()).lastModified();
                return (x < y) ? 1 : ((x == y) ? 0 : -1);
            }
        });

    }


    public void release() {
        onSearchListener = null;
        applicationContext = null;
    }


    public boolean isRunning() {
        return isRunning();
    }
}
