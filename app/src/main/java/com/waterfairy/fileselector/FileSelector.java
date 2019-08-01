package com.waterfairy.fileselector;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-08-01 16:21
 * @info:
 */
public class FileSelector {
    private Activity activity;
    private Fragment fragment;
    private FileSelectOptions options;

    public FileSelector(Activity activity) {
        this.activity = activity;
    }

    public FileSelector(Fragment fragment) {
        this.fragment = fragment;
    }

    public static FileSelector with(Activity activity) {
        return new FileSelector(activity);
    }

    public static FileSelector with(Fragment fragment) {
        return new FileSelector(fragment);
    }

    public FileSelector option(FileSelectOptions options) {
        this.options = options;
        return this;
    }

    public void execute(int requestCode) {
        if (options == null) {
            new Exception("请添加options").printStackTrace();
        } else {
            if (activity == null && fragment == null) {
                new Exception("请设置activity").printStackTrace();
            } else {
                if (fragment != null) {
                    fragment.startActivityForResult(intent(), requestCode);
                } else {
                    activity.startActivityForResult(intent(), requestCode);
                }
            }
        }
    }

    /**
     * 获取intent  附带跳转class
     *
     * @return
     */
    private Intent intent() {
        if (options == null) {
            new Exception("请添加options").printStackTrace();
            return new Intent();
        } else {
            Intent intent = new Intent(activity, SelectFileActivity.class);
            intent.putExtra(FileSelectOptions.SCREEN_ORIENTATION, options.getScreenOrientation());
            intent.putExtra(FileSelectOptions.OPTIONS_BEAN, options);
            return intent;
        }
    }
}
