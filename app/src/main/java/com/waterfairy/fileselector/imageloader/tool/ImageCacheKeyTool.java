package com.waterfairy.fileselector.imageloader.tool;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/6/3 09:57
 * @info:
 */
public class ImageCacheKeyTool {
    /**
     * @param path         图片路径
     * @param viewWidth    view 宽
     * @param viewHeight   view 高
     * @param transformKey transform 中的 getKey()  (多个transform 按顺序拼接)
     * @return
     */
    public static String getImageCacheKey(String path, int viewWidth, int viewHeight, String transformKey) {
        return MD5Utils.getMD5Code(path + ";" +
                "wh:" + viewWidth + "-" + viewHeight + ";" +
                "transform:" + transformKey);
    }
}
