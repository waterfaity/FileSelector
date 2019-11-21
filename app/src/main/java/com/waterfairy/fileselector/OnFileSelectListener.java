package com.waterfairy.fileselector;

import java.io.File;
import java.util.HashMap;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-11-21 16:48
 * @info:
 */
public interface OnFileSelectListener {
    void onFileSelect(HashMap<String, File> selectFiles);
}
