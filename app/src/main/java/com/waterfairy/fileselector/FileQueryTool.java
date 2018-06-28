package com.waterfairy.fileselector;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileQueryTool {

    private int currentLevel;
    private String selectType;

    public interface OnFileQueryListener {
        void onQueryFile(FileListBean fileListBean);
    }

    private OnFileQueryListener onFileQueryListener;

    public void setOnFileQueryListener(OnFileQueryListener onFileQueryListener) {
        this.onFileQueryListener = onFileQueryListener;
    }

    private HashMap<Integer, FileListBean> fileHashMap;

    public FileQueryTool() {
        fileHashMap = new HashMap<>();
    }

    public void queryFileNext(File file) {
        currentLevel++;
        queryFile(file, currentLevel);
    }

    /**
     * 查询文件list
     *
     * @param file
     * @param level
     */
    public void queryFile(File file, int level) {
        if (file == null) {
            return;
        }
        FileListBean fileListBean = new FileListBean();
        fileListBean.setFile(file);
        fileListBean.setLevel(level);
        if (file.exists()) {
            fileListBean.setFileList(getFiles(file, selectType));
        } else {
            ToastShowTool.show("文件不存在");
        }
        currentLevel = level;
        fileHashMap.put(level, fileListBean);
        if (onFileQueryListener != null) onFileQueryListener.onQueryFile(fileListBean);
    }

    /**
     * 获取文件夹下的符合类型的文件
     *
     * @param file
     * @return
     */
    private File[] getFiles(File file, String selectType) {
        if (TextUtils.isEmpty(selectType)) return file.listFiles();
        else {
            File[] files = file.listFiles();
            List<File> queryFileList = new ArrayList<>();
            if (files != null && files.length > 0) {
                for (File fileTemp : files) {
                    if (fileTemp.isDirectory()) {
                        queryFileList.add(fileTemp);
                    } else {
                        String typeTemp = FileUtils.getType(fileTemp.getName());
                        if (!TextUtils.isEmpty(typeTemp)) {
                            if (FileUtils.isType(typeTemp, selectType)) {
                                queryFileList.add(fileTemp);
                            }
                        }
                    }
                }
            }
            return queryFileList.toArray(new File[queryFileList.size()]);
        }
    }

    public void back() {
        if (currentLevel == 0) return;
        currentLevel--;
        if (onFileQueryListener != null)
            onFileQueryListener.onQueryFile(fileHashMap.get(currentLevel));
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }
}
