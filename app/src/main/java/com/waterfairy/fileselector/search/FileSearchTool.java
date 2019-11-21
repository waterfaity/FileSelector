package com.waterfairy.fileselector.search;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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


public class FileSearchTool {
    private ArrayList<FolderSearchBean> fileList = new ArrayList<>();
    private boolean running;
    private OnSearchListener onSearchListener;
    private FileSearchConfig config;


    private FileSearchTool() {

    }

    public FileSearchTool setConfig(FileSearchConfig config) {
        this.config = config;
        return this;
    }


    public static FileSearchTool newInstance() {
        return new FileSearchTool();
    }


    public void start() {
        if (config == null) {
            if (onSearchListener != null) {
                onSearchListener.onSearchError("请设置config!");
            }
        } else if (config.getExtensions() == null || config.getExtensions().length == 0) {
            if (onSearchListener != null) {
                onSearchListener.onSearchError("请设置搜索文件类型!");
            }
        } else if (config.getSearchDeep() <= 0) {
            if (onSearchListener != null) {
                onSearchListener.onSearchError("搜索文件层次必须大于等于1!");
            }
        } else {
            running = true;
            fileList.clear();
            startAsyncTask();
        }
    }

    /**
     * 异步搜索
     */
    private void startAsyncTask() {
        new AsyncTask<Void, String, ArrayList<FolderSearchBean>>() {

            @Override
            protected ArrayList<FolderSearchBean> doInBackground(Void... voids) {
                OnSearchListener onSearchListener = new OnSearchListener() {
                    @Override
                    public void onSearch(String path) {
                        publishProgress(path);
                    }

                    @Override
                    public void onSearchSuccess(ArrayList<FolderSearchBean> fileList) {

                    }

                    @Override
                    public void onSearchError(String errorMsg) {

                    }
                };
                //搜索外置sd卡
                search(Environment.getExternalStorageDirectory(), 0, onSearchListener);
                //搜索指定文件夹
                searchSpePaths(onSearchListener);
                //移出排除的文件夹
                removeSpePaths();
                //排序
                sortByTime(fileList);
                //合并所有的文件
                if (config.isMerge())
                    mergeFiles();
                return fileList;
            }

            /**
             * 合并所有文件 生成一个 全部图片  的集合
             */
            private void mergeFiles() {
                //加入全部文件
                if (fileList != null && fileList.size() > 0) {
                    FolderSearchBean searchImgBean = new FolderSearchBean();
                    searchImgBean.setIsAll(true);
                    for (int i = 0; i < fileList.size(); i++) {
                        FolderSearchBean searchFolderBean = fileList.get(i);
                        List<FileSearchBean> searchImgBeans = FileSearchTool.newInstance().setConfig(config).searchFolder(searchFolderBean);
                        searchImgBean.addChildImageBeans(searchImgBeans);
                    }
                    fileList.add(0, searchImgBean);
                }
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
        }.execute();
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
                        if (TextUtils.equals(searchFolderBean.getPath(), config.getIgnorePaths()[j])) {
                            fileList.remove(searchFolderBean);
                            i--;
                        }
                    }
                }
            }
        }
    }

    /**
     * 搜索额外的路径
     *
     * @param onSearchListener
     */
    private void searchSpePaths(OnSearchListener onSearchListener) {

        if (config.getSearchPaths() != null && config.getSearchPaths().length > 0) {
            //创建新的集合   判断是否已经存在  并移出已经搜索过的路径

            List<String> tempPaths = Arrays.asList(config.getSearchPaths());

            if (fileList != null && fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    //已经搜索到的数据
                    FolderSearchBean searchFolderBean = fileList.get(i);
                    for (int j = 0; j < tempPaths.size(); j++) {
                        //指定的数据路径
                        String tempPath = tempPaths.get(j);
                        if (!TextUtils.isEmpty(tempPath)) {
                            File tempFile = new File(tempPath);
                            if (tempFile.exists()) {
                                if (TextUtils.equals(searchFolderBean.getPath(), tempFile.getAbsolutePath())) {
                                    //移出 并跳出
                                    tempPaths.remove(tempPath);
                                    break;
                                }
                            }
                        }
                    }
                    if (tempPaths.size() == 0) break;
                }
            }

            if (tempPaths.size() > 0) {
                //继续搜索
                for (int i = 0; i < tempPaths.size(); i++) {
                    String path = tempPaths.get(i);
                    //不为空
                    if (!TextUtils.isEmpty(path)) {
                        File file = new File(tempPaths.get(i));
                        //文件存在 并且是路径
                        if (file.exists() && file.isDirectory()) {
                            search(file, config.getSearchDeep() - 1, onSearchListener);
                        }
                    }
                }
            }
        }
    }

    public void stop() {
        running = false;
    }

    /**
     * 排除第一级文件夹:Android
     * 排除开头为.的文件夹
     *
     * @param file
     * @param deep
     * @param onSearchListener
     */
    private void search(File file, int deep, OnSearchListener onSearchListener) {
        //文件夹存在 并在deep范围内
        if (file.exists() && deep < config.getSearchDeep() && !file.getName().startsWith(".") && !(deep == 0 && TextUtils.equals("Android", file.getName()))) {
            File[] list = file.listFiles();
            if (list != null) {
                //作为一个搜索限制 如果该文件夹A存在符合条件的文件AB 记录该文件夹A和文件AB ,然后跳过该文件夹A搜索下一个文件夹
                boolean jump = false;
                //遍历该文件夹下的所有文件及文件夹
                for (File childFile : list) {
                    if (childFile.isDirectory()) {
                        if (childFile.getName().startsWith(".")) continue;
                        else
                            //是文件夹->继续扫描下一级文件夹
                            search(childFile, deep + 1, onSearchListener);
                    } else if (!jump) {
                        //是文件 并且不跳过
                        String childAbsolutePath = childFile.getAbsolutePath();
                        for (String anExtension : config.getExtensions()) {
                            if (childAbsolutePath.endsWith(anExtension)) {
                                fileList.add(new FolderSearchBean(file.getAbsolutePath(), childAbsolutePath));
                                jump = true;
                                break;
                            }
                        }
                        if (jump) {
                            //如果有跳过 说明搜索到了符合条件的文件
                            if (onSearchListener != null)
                                onSearchListener.onSearch(childAbsolutePath);
                        }
                    }
                }
            }
        }
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
        List<FileSearchBean> imgBeans = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    String childPath = file.getAbsolutePath();
                    for (String anExtension : config.getExtensions()) {
                        if (childPath.endsWith(anExtension)) {
                            imgBeans.add(new FileSearchBean(childPath));
                        }
                    }
                }
            }
        }

        return imgBeans;
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
    }


    public boolean isRunning() {
        return isRunning();
    }
}
