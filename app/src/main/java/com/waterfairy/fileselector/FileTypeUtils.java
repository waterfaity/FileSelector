package com.waterfairy.fileselector;

import android.text.TextUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/1 18:23
 * @info:
 */
public class FileTypeUtils {
    public static int getIcon(String name) {
        if (!TextUtils.isEmpty(name)) {
            int index = name.lastIndexOf(".");
            String type = name.substring(index + 1, name.length());
            if (!TextUtils.isEmpty(type)) {
                type = "," + type + ",";
                if ((",MP3,WMA,FLAC,AAC,MMF,AMR,M4A,M4R,OGG,MP2,WAV,WV," +
                        "mp3,wma,flac,aac,mmf,amr,m4a,m4r,ogg,mp2,wav,wv").contains(type)) {
                    return R.mipmap.ic_audio;
                } else if ((",PNG,JEPG,JPG,BMP,GIF," +
                        "png,jepg,jpg,bmp,gif,").contains(type)) {
                    return R.mipmap.ic_img;
                } else if ((",RM,RMVB,MPEG1,MPEG2,MPEG3,MPEG4,MOV,MTV,DAT,WMV,AVI,3GP,AMV,DMV,FLV," +
                        "rm,rmvb,mpeg1,mpeg2,mpeg3,mpeg4,mov,mtv,dat,wmv,avi,3gp,amv,dmv,flv,").contains(type)) {
                    return R.mipmap.ic_video;
                } else if (",PDF,pdf,".contains(type)) {
                    return R.mipmap.ic_pdf;
                } else if (",doc,DOC,".contains(type)) {
                    return R.mipmap.ic_word;
                } else if (",ppt,pptx,PPT,PPTX,".contains(type)) {
                    return R.mipmap.ic_powerpoint;
                } else if (",exl,EXL,".contains(type)) {
                    return R.mipmap.ic_excel;
                } else if (",TEXT,text,txt,TXT,".contains(type)) {
                    return R.mipmap.ic_text;
                } else if (",HTML,html,".contains(type)) {
                    return R.mipmap.ic_html;
                } else if (",DB,db,".contains(type)) {
                    return R.mipmap.ic_database;
                }
            }
        }

        return R.mipmap.ic_unknown;
    }
}
