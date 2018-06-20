package com.waterfairy.fileselector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class SelectFileActivity extends AppCompatActivity {
    private Fragment selectFileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        initFragment();
    }

    private void initFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            selectFileFragment = fragments.get(0);
        }
    }
}
