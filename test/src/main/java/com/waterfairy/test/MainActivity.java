package com.waterfairy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.waterfairy.fileselector.SelectFileFragment;
import com.waterfairy.utils.PermissionUtils;
import com.waterfairy.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private SelectFileFragment selectFileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_STORAGE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFileFragment = (SelectFileFragment) getFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            selectFileFragment.back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST, 1, "文件开关");
        menu.add(0, Menu.FIRST + 1, 1, "文件夹开关");
        menu.add(0, Menu.FIRST + 2, 1, "移除所有");
        menu.add(0, Menu.FIRST + 3, 1, "移除当前文件夹所有");
        menu.add(0, Menu.FIRST + 4, 1, "添加所有");
        menu.add(0, Menu.FIRST + 5, 1, "完成");
        menu.add(0, Menu.FIRST + 6, 1, "只能添加本文件夹的文件");
        return super.onCreateOptionsMenu(menu);
    }

    private boolean canSelect;
    private boolean canSelectDir;
    private boolean canOnlySelectCurrentDir=true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case Menu.FIRST:
                selectFileFragment.setCanSelect(canSelect = !canSelect);
                break;
            case Menu.FIRST + 1:
                selectFileFragment.setCanSelectDir(canSelectDir = !canSelectDir);
                break;
            case Menu.FIRST + 2:
                selectFileFragment.removeAll();
                break;
            case Menu.FIRST + 3:
                selectFileFragment.removeCurrentDirAllFiles();
                break;
            case Menu.FIRST + 4:
                selectFileFragment.selectAllFiles();
                break;
            case Menu.FIRST + 5:
                HashMap<String, File> selectFiles = selectFileFragment.getSelectFiles();
                if (selectFiles != null)
                    ToastUtils.show(selectFiles.size() + "");
                else ToastUtils.show("0");
                break;
            case Menu.FIRST+6:
                selectFileFragment.setCanOnlySelectCurrentDir(canOnlySelectCurrentDir=!canOnlySelectCurrentDir);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
