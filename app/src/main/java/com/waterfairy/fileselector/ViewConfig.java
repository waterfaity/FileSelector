package com.waterfairy.fileselector;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/27 18:51
 * @info:
 */
public class ViewConfig implements Parcelable {
    private Rect backPadding;//返回按钮 padding
    private int backRes;//返回按钮 图标资源
    private int actionBarHeight;//actionBar  高度
    private int menuHeight;//确认按钮高度
    private int menuBgRes;//确认按钮背景颜色
    private int menuMarginRight=-1;
    private String title;

    public int getMenuMarginRight() {
        return menuMarginRight;
    }

    public ViewConfig setMenuMarginRight(int menuMarginRight) {
        this.menuMarginRight = menuMarginRight;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ViewConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getMenuBgRes() {
        return menuBgRes;
    }

    public ViewConfig setMenuBgRes(int menuBgRes) {
        this.menuBgRes = menuBgRes;
        return this;
    }

    public Rect getBackPadding() {
        return backPadding;
    }

    public ViewConfig setBackPadding(Rect backPadding) {
        this.backPadding = backPadding;
        return this;
    }

    public int getMenuHeight() {
        return menuHeight;
    }

    public ViewConfig setMenuHeight(int menuHeight) {
        this.menuHeight = menuHeight;
        return this;
    }


    public int getBackRes() {
        return backRes;
    }

    public ViewConfig setBackRes(int backRes) {
        this.backRes = backRes;
        return this;
    }

    public int getActionBarHeight() {
        return actionBarHeight;
    }

    public ViewConfig setActionBarHeight(int actionBarHeight) {
        this.actionBarHeight = actionBarHeight;
        return this;
    }

    public ViewConfig() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.backPadding, flags);
        dest.writeInt(this.backRes);
        dest.writeInt(this.actionBarHeight);
        dest.writeInt(this.menuHeight);
        dest.writeInt(this.menuBgRes);
        dest.writeInt(this.menuMarginRight);
        dest.writeString(this.title);
    }

    protected ViewConfig(Parcel in) {
        this.backPadding = in.readParcelable(Rect.class.getClassLoader());
        this.backRes = in.readInt();
        this.actionBarHeight = in.readInt();
        this.menuHeight = in.readInt();
        this.menuBgRes = in.readInt();
        this.menuMarginRight = in.readInt();
        this.title = in.readString();
    }

    public static final Creator<ViewConfig> CREATOR = new Creator<ViewConfig>() {
        @Override
        public ViewConfig createFromParcel(Parcel source) {
            return new ViewConfig(source);
        }

        @Override
        public ViewConfig[] newArray(int size) {
            return new ViewConfig[size];
        }
    };
}
