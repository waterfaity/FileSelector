package com.waterfairy.fileselector;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

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
        return new FileSelectOptions()
                .setCanOpenFile(true)
                .setCanSelect(true)
                .setMaxFileSize(10 * 1024 * 1024)//2M
                .setPathAuthority(ProviderUtils.authority)
                .setSearchStyle(FileSelectOptions.STYLE_ONLY_FILE)
                .setSelectType(",mp4,mp3")
                .setSortType(FileSelectOptions.SORT_BY_NAME)
                .setIgnorePaths(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WisDomCloud");//FileSelectOptions.STYLE_ONLY_FILE
    }
}
