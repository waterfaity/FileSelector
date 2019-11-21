package com.waterfairy.fileselector.search;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-11-21 16:00
 * @info:
 */
public interface OnSearchListener {
    /**
     * 搜索某个文件夹
     *
     * @param path
     */
    void onSearch(String path);

    /**
     * 搜索成功
     *
     * @param fileList
     */
    void onSearchSuccess(ArrayList<FolderSearchBean> fileList);

    /**
     * 搜索失败
     *
     * @param errorMsg
     */
    void onSearchError(String errorMsg);
}
