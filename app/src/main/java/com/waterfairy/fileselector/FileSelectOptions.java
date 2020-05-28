package com.waterfairy.fileselector;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
/**
 * 修改主题色
 * <color name="fileSelectorColorPrimary">#008577</color>
 * <color name="fileSelectorColorPrimaryDark">#00574B</color>
 * <color name="fileSelectorColorAccent">#D81B60</color>
 */

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-08-01 16:26
 * @info:
 */
public class FileSelectOptions implements Parcelable {

    public static final String SCREEN_ORIENTATION = "screen_orientation";
    public static final String OPTIONS_BEAN = "option_bean";

    private int limitNum = -1;//数量限制
    private long maxFileSize;//文件大小  B
    private boolean showThumb;//false 展示默认图标 / true 显示预览图
    private String selectType;//如:( ,png, )  或: ( ,jpg,png, ) (不要括号)
    private boolean canSelect = true;//是否可以选择
    private boolean canOpenFile = false;//是否点击预览文件
    private boolean canSelectDir = false;//是否可以选择文件夹
    private String pathAuthority;// fileProvider
    private String[] ignorePaths;//FileSelectOptions.STYLE_ONLY_FILE有效
    private boolean showHiddenFile = false;//显示隐藏文件
    private boolean canOnlySelectCurrentDir = false;//只允许选择当前文件夹内的文件

    private ViewConfig viewConfig;//view 配置


    /**
     * 方向
     */
    public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
    public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
    private int screenOrientation = -1;

    /**
     * 排序
     */
    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_NAME_DESC = 2;
    public static final int SORT_BY_TIME = 3;
    public static final int SORT_BY_TIME_DESC = 4;
    private int sortType = SORT_BY_NAME;

    /**
     * 文件搜索类型
     */
    public static final int STYLE_ONLY_FILE = 1;//只有指定文件
    public static final int STYLE_FOLDER_AND_FILE = 0;//文件夹和文件
    private int searchStyle;

    public int getSearchStyle() {
        return searchStyle;
    }

    public FileSelectOptions setSearchStyle(int searchStyle) {
        this.searchStyle = searchStyle;
        return this;
    }

    public String[] getIgnorePaths() {
        return ignorePaths;
    }

    public FileSelectOptions setIgnorePaths(String... ignorePaths) {
        this.ignorePaths = ignorePaths;
        return this;
    }

    public boolean isCanOpenFile() {
        return canOpenFile;
    }

    public FileSelectOptions setCanOpenFile(boolean canOpenFile) {
        this.canOpenFile = canOpenFile;
        return this;
    }

    public boolean isShowHiddenFile() {
        return showHiddenFile;
    }

    public String getPathAuthority() {
        return pathAuthority;
    }

    public FileSelectOptions setPathAuthority(String pathAuthority) {
        this.pathAuthority = pathAuthority;
        return this;
    }

    public void setShowHiddenFile(boolean showHiddenFile) {
        this.showHiddenFile = showHiddenFile;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public FileSelectOptions setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
        return this;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public FileSelectOptions setLimitNum(int limitNum) {
        this.limitNum = limitNum;
        return this;
    }

    public boolean isCanSelect() {
        return canSelect;
    }

    public FileSelectOptions setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
        return this;
    }

    public boolean isCanSelectDir() {
        return canSelectDir;
    }

    public FileSelectOptions setCanSelectDir(boolean canSelectDir) {
        this.canSelectDir = canSelectDir;
        return this;
    }

    public String getSelectType() {
        return selectType;
    }

    public FileSelectOptions setSelectType(String selectType) {
        this.selectType = selectType;
        return this;
    }

    public String[] getExtensions() {
        if (!TextUtils.isEmpty(selectType)) {
            String[] split = selectType.split(",");
            ArrayList<String> datas = new ArrayList<>();
            for (String s : split) {
                if (!TextUtils.isEmpty(s)) {
                    datas.add("." + s);
                }
            }
            String[] extensions = new String[datas.size()];
            for (int i = 0; i < datas.size(); i++) {
                extensions[i] = datas.get(i);
            }
            return extensions;
        }
        return new String[]{};
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public FileSelectOptions setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    public int getSortType() {
        return sortType;
    }

    public FileSelectOptions setSortType(int sortType) {
        this.sortType = sortType;
        return this;
    }

    public boolean isShowThumb() {
        return showThumb;
    }

    public FileSelectOptions setShowThumb(boolean showThumb) {
        this.showThumb = showThumb;
        return this;
    }

    public ViewConfig getViewConfig() {
        return viewConfig;
    }

    public FileSelectOptions setViewConfig(ViewConfig viewConfig) {
        this.viewConfig = viewConfig;
        return this;
    }

    public boolean isCanOnlySelectCurrentDir() {
        return canOnlySelectCurrentDir;
    }

    public FileSelectOptions setCanOnlySelectCurrentDir(boolean canOnlySelectCurrentDir) {
        this.canOnlySelectCurrentDir = canOnlySelectCurrentDir;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.limitNum);
        dest.writeLong(this.maxFileSize);
        dest.writeByte(this.showThumb ? (byte) 1 : (byte) 0);
        dest.writeString(this.selectType);
        dest.writeByte(this.canSelect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canOpenFile ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canSelectDir ? (byte) 1 : (byte) 0);
        dest.writeString(this.pathAuthority);
        dest.writeStringArray(this.ignorePaths);
        dest.writeByte(this.showHiddenFile ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canOnlySelectCurrentDir ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.viewConfig, flags);
        dest.writeInt(this.screenOrientation);
        dest.writeInt(this.sortType);
        dest.writeInt(this.searchStyle);
    }

    public FileSelectOptions() {
    }

    protected FileSelectOptions(Parcel in) {
        this.limitNum = in.readInt();
        this.maxFileSize = in.readLong();
        this.showThumb = in.readByte() != 0;
        this.selectType = in.readString();
        this.canSelect = in.readByte() != 0;
        this.canOpenFile = in.readByte() != 0;
        this.canSelectDir = in.readByte() != 0;
        this.pathAuthority = in.readString();
        this.ignorePaths = in.createStringArray();
        this.showHiddenFile = in.readByte() != 0;
        this.canOnlySelectCurrentDir = in.readByte() != 0;
        this.viewConfig = in.readParcelable(ViewConfig.class.getClassLoader());
        this.screenOrientation = in.readInt();
        this.sortType = in.readInt();
        this.searchStyle = in.readInt();
    }

    public static final Creator<FileSelectOptions> CREATOR = new Creator<FileSelectOptions>() {
        @Override
        public FileSelectOptions createFromParcel(Parcel source) {
            return new FileSelectOptions(source);
        }

        @Override
        public FileSelectOptions[] newArray(int size) {
            return new FileSelectOptions[size];
        }
    };
}
