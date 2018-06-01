package com.waterfairy.test;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.waterfairy.fileselector.ImageViewTintUtils;
import com.waterfairy.fileselector.SelectFileFragment;
import com.waterfairy.utils.PermissionUtils;

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
}
