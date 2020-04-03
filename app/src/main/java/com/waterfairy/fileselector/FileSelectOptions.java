package com.waterfairy.fileselector;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-08-01 16:26
 * @info:
 */
public class FileSelectOptions implements Serializable {

    public static final String SCREEN_ORIENTATION = "screen_orientation";
    public static final String OPTIONS_BEAN = "option_bean";
    public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
    public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
    private final long serialVersionUID = 20190801171010000L;
    private int screenOrientation = -1;
    private int limitNum = -1;
    private boolean canSelect = true;//是否可以选择
    private boolean canSelectDir = false;//是否可以选择文件夹
    private String pathAuthority;//
    private String selectType;//如:( ,png, )  或: ( ,jpg,png, ) (不要括号)
    private boolean canOnlySelectCurrentDir = false;//只允许选择当前文件夹内的文件
    private boolean showHiddenFile = false;//显示隐藏文件
    private boolean canOpenFile = false;//显示隐藏文件
    private String[] ignorePaths;//FileSelectOptions.STYLE_ONLY_FILE有效

    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_NAME_DESC = 2;
    public static final int SORT_BY_TIME = 3;
    public static final int SORT_BY_TIME_DESC = 4;

    private int sortType = SORT_BY_NAME;


    public static final int STYLE_ONLY_FILE = 1;//只有指定文件
    public static final int STYLE_FOLDER_AND_FILE = 0;//文件夹和文件
    /**
     * 文件搜索类型
     */
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

    public int getSortType() {
        return sortType;
    }

    public FileSelectOptions setSortType(int sortType) {
        this.sortType = sortType;
        return this;
    }

    public boolean isCanOnlySelectCurrentDir() {
        return canOnlySelectCurrentDir;
    }

    public FileSelectOptions setCanOnlySelectCurrentDir(boolean canOnlySelectCurrentDir) {
        this.canOnlySelectCurrentDir = canOnlySelectCurrentDir;
        return this;
    }
}
