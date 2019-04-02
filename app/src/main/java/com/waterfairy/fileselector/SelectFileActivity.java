package com.waterfairy.fileselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectFileActivity extends AppCompatActivity {
    private SelectFileFragment selectFileFragment;
    public static final String RESULT_DATA = "data";
    public static final int NO_LIMIT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        initFragment();
    }

    private void initFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            selectFileFragment = (SelectFileFragment) fragments.get(0);
        }
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
    protected void onResume() {
        super.onResume();
        ToastShowTool.initToast(this);
    }
}
