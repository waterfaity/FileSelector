package com.waterfairy.fileselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class FileSelectActivity extends AppCompatActivity {
    public static final int NO_LIMIT = -1;
    private FileSelectFragment selectFileFragment;
    public static final String RESULT_DATA = "data";
    private FileSelectOptions options;
    private Button mEnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra();
        initTheme();
        initScreen();
        setContentView(R.layout.activity_selector);
        initFragment();
        initView();
    }

    private void initTheme() {
        if (options != null) {
            int themeStyle = options.getThemeStyle();
            if (themeStyle != 0) {
                setTheme(themeStyle);
            }
        }
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
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        ArrayList<String> selectFileList = selectFileFragment.getSelectFilePathList();
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
