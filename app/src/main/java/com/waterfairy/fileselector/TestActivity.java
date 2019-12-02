package com.waterfairy.fileselector;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.waterfairy.fileselector.search.FileSearchConfig;
import com.waterfairy.fileselector.search.FileSearchTool;
import com.waterfairy.fileselector.search.FileSearchTool2;
import com.waterfairy.fileselector.search.FolderSearchBean;
import com.waterfairy.fileselector.search.OnSearchListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FileSearchTool2 fileSearchTool2 = FileSearchTool2.newInstance(this);
        FileSearchTool fileSearchTool = FileSearchTool.newInstance();

        FileSearchConfig fileSearchConfig = FileSearchConfig.defaultInstance();
        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//content://media/external/images/media
//        Uri externalContentUri = MediaStore.Files.getContentUri("external");//content://media/external/file
        Log.i(TAG, "onCreate: " + externalContentUri);
        fileSearchConfig.setContentUri(externalContentUri);
        fileSearchConfig.setSearchDeep(5);
        fileSearchConfig.setExtensions(".jpg", ".png", ".gif");
        fileSearchConfig.setMerge(true);
        fileSearchTool.setConfig(fileSearchConfig);
        fileSearchTool.setOnSearchListener(new OnSearchListener() {
            @Override
            public void onSearch(String path) {
                Log.i(TAG, "onSearch: " + path);
            }

            @Override
            public void onSearchSuccess(ArrayList<FolderSearchBean> fileList) {
                Log.i(TAG, "onSearchSuccess: ");
            }

            @Override
            public void onSearchError(String errorMsg) {
                Log.i(TAG, "onSearchError: ");
            }
        });
        fileSearchTool.start();
    }
}
