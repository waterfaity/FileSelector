package com.waterfairy.fileselector;

import android.app.Activity;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_selector_activity_test);

        FileSearchConfig fileSearchConfig = FileSearchConfig.defaultInstance();
        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//content://media/external/images/media
//        Uri externalContentUri = MediaStore.Files.getContentUri("external");//content://media/external/file
        Log.i(TAG, "onCreate: " + externalContentUri);
        fileSearchConfig.setContentUri(externalContentUri);
        fileSearchConfig.setExtensions(".jpg", ".png", ".gif");
        getVideo(this, 12);


    }


    private void getVideo(Activity activity, int limitNum) {
        FileSelector.with(activity).option(getFileSelectOptions().setShowThumb(true).setLimitNum(limitNum)).execute(1001);
    }

    private FileSelectOptions getFileSelectOptions() {

        float density = getResources().getDisplayMetrics().density;
        ViewConfig viewConfig = new ViewConfig();
        int padding = (int) (density * 15);
        viewConfig.setBackPadding(new Rect(padding, padding, padding, padding));
        viewConfig.setActionBarHeight((int) (density * 50));
        viewConfig.setBackRes(R.mipmap.file_selector_ic_back);
        viewConfig.setMenuHeight((int) (density * 20));
        return new FileSelectOptions()
                .setCanOpenFile(true)
                .setViewConfig(viewConfig)
                .setCanSelect(true)
                .setMaxFileSize(10 * 1024 * 1024)//2M
                .setPathAuthority(ProviderUtils.authority)
                .setSearchStyle(FileSelectOptions.STYLE_ONLY_FILE)
                .setSelectType(",mp4,mp3")
                .setSortType(FileSelectOptions.SORT_BY_NAME)
                .setIgnorePaths(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WisDomCloud");//FileSelectOptions.STYLE_ONLY_FILE
    }
}
