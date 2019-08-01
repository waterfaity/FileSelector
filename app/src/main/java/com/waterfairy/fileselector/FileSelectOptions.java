package com.waterfairy.fileselector;

import java.io.Serializable;

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
    private boolean canSelect = true;
    private boolean canSelectDir = false;
    private String pathAuthority;
    private String selectType;//如:( ,png, )  或: ( ,jpg,png, ) (不要括号)
    private boolean canOnlySelectCurrentDir = false;
    private boolean showHiddenFile=false;

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

    public boolean isCanOnlySelectCurrentDir() {
        return canOnlySelectCurrentDir;
    }

    public FileSelectOptions setCanOnlySelectCurrentDir(boolean canOnlySelectCurrentDir) {
        this.canOnlySelectCurrentDir = canOnlySelectCurrentDir;
        return this;
    }
}
