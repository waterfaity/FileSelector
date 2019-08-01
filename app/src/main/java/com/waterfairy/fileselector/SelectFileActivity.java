package com.waterfairy.fileselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectFileActivity extends AppCompatActivity {
    private SelectFileFragment selectFileFragment;
    public static final String RESULT_DATA = "data";
    public static final int NO_LIMIT = -1;
    private FileSelectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScreen();
        setContentView(R.layout.activity_selector);
        getExtra();
        initFragment();
    }


    private void initScreen() {
        int intExtra = getIntent().getIntExtra(FileSelectOptions.SCREEN_ORIENTATION, FileSelectOptions.SCREEN_ORIENTATION_PORTRAIT);
        if (intExtra == FileSelectOptions.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (intExtra == FileSelectOptions.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void getExtra() {
        Intent intent = getIntent();
        options = (FileSelectOptions) intent.getSerializableExtra(FileSelectOptions.OPTIONS_BEAN);
    }

    private void initFragment() {

        selectFileFragment = new SelectFileFragment();

        if (options != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(FileSelectOptions.OPTIONS_BEAN, options);
            selectFileFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, selectFileFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onClickComplete();
        return super.onOptionsItemSelected(item);
    }

    private void onClickComplete() {
        ArrayList<File> selectFileList = selectFileFragment.getSelectFileList();
        if (selectFileList == null || selectFileList.size() == 0) {
            ToastShowTool.show("请选择文件");
        } else {
            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA, selectFileList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("完成").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (selectFileFragment != null) {
            if (selectFileFragment.onKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastShowTool.initToast(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastShowTool.release();
    }
}
