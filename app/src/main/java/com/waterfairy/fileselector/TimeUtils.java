package com.waterfairy.fileselector;

import java.text.SimpleDateFormat;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/1 19:48
 * @info:
 */
public class TimeUtils {
    public static String format(long time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(time);
    }
}
