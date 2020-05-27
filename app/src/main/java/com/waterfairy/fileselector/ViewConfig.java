package com.waterfairy.fileselector;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2020/5/27 18:51
 * @info:
 */
public class ViewConfig implements Parcelable {
    private Rect backPadding;
    private int backRes;
    private int actionBarHeight;
    private int menuHeight;

    public Rect getBackPadding() {
        return backPadding;
    }

    public void setBackPadding(Rect backPadding) {
        this.backPadding = backPadding;
    }

    public int getMenuHeight() {
        return menuHeight;
    }

    public void setMenuHeight(int menuHeight) {
        this.menuHeight = menuHeight;
    }



    public int getBackRes() {
        return backRes;
    }

    public void setBackRes(int backRes) {
        this.backRes = backRes;
    }

    public int getActionBarHeight() {
        return actionBarHeight;
    }

    public void setActionBarHeight(int actionBarHeight) {
        this.actionBarHeight = actionBarHeight;
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
    }

    public ViewConfig() {
    }

    protected ViewConfig(Parcel in) {
        this.backPadding = in.readParcelable(Rect.class.getClassLoader());
        this.backRes = in.readInt();
        this.actionBarHeight = in.readInt();
        this.menuHeight = in.readInt();
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
