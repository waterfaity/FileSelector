package com.waterfairy.fileselector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/1 18:23
 * @info:
 */
public class FileUtils {


    private static HashMap<String, Integer> hashMap;


    public static HashMap<String, Integer> getHashMap() {
        if (hashMap == null) {
            initHashMap();
        }
        return hashMap;
    }

    public static final String TYPE_AUDIO = ",MP3,WMA,FLAC,AAC,MMF,AMR,M4A,M4R,OGG,MP2,WAV,WV," +
            "mp3,wma,flac,aac,mmf,amr,m4a,m4r,ogg,mp2,wav,wv,";
    public static final String TYPE_VIDEO = ",RM,RMVB,MPEG1,MPEG2,MPEG3,MPEG4,MOV,MTV,WMV,AVI,3GP,AMV,DMV,FLV," +
            "rm,rmvb,mpeg1,mpeg2,mpeg3,mpeg4,mov,mtv,wmv,avi,3gp,amv,dmv,flv,mp4,";
    public static final String TYPE_JPG = ",PNG,JEPG,JPG,BMP,GIF," +
            "png,jepg,jpg,bmp,gif,";

    public static int getIcon(String name) {
        if (hashMap == null) {
            initHashMap();
        }
        if (!TextUtils.isEmpty(name)) {
            String type = getType(name);
            if (!TextUtils.isEmpty(type)) {
                type = "," + type + ",";
                if (TYPE_AUDIO.contains(type)) {
                    return hashMap.get("audio");
                } else if (TYPE_JPG.contains(type)) {
                    return hashMap.get("image");
                } else if (TYPE_VIDEO.contains(type)) {
                    return hashMap.get("video");
                } else if (",PDF,pdf,".contains(type)) {
                    return hashMap.get("pdf");
                } else if (",doc,DOC,docx,DOCX,".contains(type)) {
                    return hashMap.get("word");
                } else if (",ppt,pptx,PPT,PPTX,".contains(type)) {
                    return hashMap.get("ppt");
                } else if (",xls,xlsx,".contains(type)) {
                    return hashMap.get("excel");
                } else if (",txt,TXT,".contains(type)) {
                    return hashMap.get("txt");
                } else if (",HTML,html,".contains(type)) {
                    return hashMap.get("html");
                } else if (",DB,db,".contains(type)) {
                    return hashMap.get("database");
                }
            }
        }
        return hashMap.get("unknown");
    }

    private static void initHashMap() {
        hashMap = new HashMap<>();
        hashMap.put("image", R.mipmap.ic_img);
        hashMap.put("pdf", R.mipmap.ic_pdf);
        hashMap.put("word", R.mipmap.ic_word);
        hashMap.put("excel", R.mipmap.ic_excel);
        hashMap.put("ppt", R.mipmap.ic_ppt);
        hashMap.put("txt", R.mipmap.ic_text);
        hashMap.put("html", R.mipmap.ic_html);
        hashMap.put("database", R.mipmap.ic_database);
        hashMap.put("video", R.mipmap.ic_video);
        hashMap.put("audio", R.mipmap.ic_audio);
        hashMap.put("unknown", R.mipmap.ic_unknown);
    }

    static String getType(String fileName) {
        if (TextUtils.isEmpty(fileName)) return "";
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1, fileName.length());
    }


    public static String getFileLen(File file) {
        long length = file.length();
        if (length < 1024) {
            return length + "B";
        } else if (length < 1024 * 1024) {
            return NumFormatUtils.getRoundingNum(length / 1024F, 2) + "KB";
        } else if (length < 1024 * 1024 * 1024) {
            return NumFormatUtils.getRoundingNum(length / (1024 * 1024F), 2) + "MB";
        } else if (length < Long.MAX_VALUE) {
            return NumFormatUtils.getRoundingNum(length / (1024 * 1024 * 1024F), 2) + "GB";
        }
        return length + "B";
    }

    public static boolean isType(String typeTemp, String allType) {
        if (TextUtils.isEmpty(allType)) return false;
        else return allType.contains(typeTemp);
    }


    //建立一个MIME类型与文件后缀名的匹配表
    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".doc", "application/msword"},
            {".docx", "application/msword"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.ms-excel"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };

    /**
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) throws FileNotFoundException {
        //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
        Intent intent = new Intent();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        Uri uri = ProviderUtils.getProviderUri(context, intent, file);
        intent.setDataAndType(/*uri*/uri, type);
        //跳转

        context.startActivity(intent);
    }

    private static String getMIMEType(File file) throws FileNotFoundException {
        if (file != null && file.exists()) {
            String name = file.getName();
            if (!TextUtils.isEmpty(name)) {
                for (String[] type : MIME_MapTable) {
                    if (name.endsWith(type[0])) {
                        return type[1];
                    }
                }
            }
        } else {
            throw new FileNotFoundException();
        }

        return "";
    }
}
