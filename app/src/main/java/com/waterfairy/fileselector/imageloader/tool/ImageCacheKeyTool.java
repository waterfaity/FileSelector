package com.waterfairy.fileselector.imageloader.tool;

import com.waterfairy.fileselector.imageloader.MD5Utils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/3 09:57
 * @info:
 */
public class ImageCacheKeyTool {
    public static String getImageCacheKey(String path, int viewWidth, int viewHeight, String transformKey) {
        return MD5Utils.getMD5Code(path + ";" +
                "wh:" + viewWidth + "-" + viewHeight + ";" +
                "transform:" + transformKey);
    }
}
