package com.waterfairy.fileselector;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/1 11:10
 * @info:
 */
public class FileListBean {
    private static final String TAG = "fileListBean";
    private int level;//层次
    private File file;
    private File[] fileList;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_NAME_DESC = 2;
    public static final int SORT_BY_TIME = 3;
    public static final int SORT_BY_TIME_DESC = 4;

    public File[] getFileList() {
        return getFileList(SORT_BY_NAME);
    }

    public File[] getFileList(final int sort) {
        final boolean aec = sort % 2 != 0;
        Arrays.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int compare1 = 0;
                int compare2 = 0;
                compare1 = Boolean.valueOf(o2.isDirectory()).compareTo(o1.isDirectory()) * 1000000;
                switch (sort) {
                    case SORT_BY_NAME:
                    case SORT_BY_NAME_DESC:
                        compare2 = o1.getName().compareTo(o2.getName());
                        break;
                    case SORT_BY_TIME:
                    case SORT_BY_TIME_DESC:
                        compare2 = o1.lastModified() - o2.lastModified() > 0 ? 1 : -1;
                        break;
                }
                return compare1 + (aec ? compare2 : -compare2);
            }
        });
        return fileList;
    }

    public void setFileList(File[] fileList) {
        this.fileList = fileList;
    }
}
