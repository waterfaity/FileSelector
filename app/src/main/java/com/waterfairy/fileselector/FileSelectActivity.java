package com.waterfairy.fileselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileSelectActivity extends AppCompatActivity {
    public static final int NO_LIMIT = -1;
    private FileSelectFragment selectFileFragment;
    public static final String RESULT_DATA = "data";
    private FileSelectOptions options;
    private Button mEnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScreen();
        setContentView(R.layout.activity_selector);
        getExtra();
        initFragment();
        initView();

    }

    private void initView() {

        mEnsure = findViewById(R.id.ensure_button);
        showEnsureButton(0);
        mEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickComplete();
            }
        });
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

        selectFileFragment = new FileSelectFragment();
        selectFileFragment.setOnFileSelectListener(new OnFileSelectListener() {
            @Override
            public void onFileSelect(HashMap<String, File> selectFiles) {
                showEnsureButton(selectFiles.size());
            }
        });

        if (options != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(FileSelectOptions.OPTIONS_BEAN, options);
            selectFileFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, selectFileFragment).commit();
    }

    private void showEnsureButton(int size) {
        if (options.getLimitNum() > 0) {
            mEnsure.setText("完成(" + size + "/" + options.getLimitNum() + ")");
        }
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