package com.waterfairy.fileselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class FileSelectActivity extends AppCompatActivity {
    public static final int NO_LIMIT = -1;
    private FileSelectFragment selectFileFragment;
    public static final String RESULT_DATA = "data";
    private FileSelectOptions options;
    private TextView mEnsure;
    private ImageView mIVBack;
    private RelativeLayout mRLActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra();
        initScreen();
        setContentView(R.layout.file_selector_activity_selector);
        initFragment();
        initView();
    }


    private void initView() {

        mIVBack = findViewById(R.id.iv_back);
        mEnsure = findViewById(R.id.tv_ensure);
        mRLActionBar = findViewById(R.id.rel_action_bar);
        showEnsureButton(0);
        mEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickComplete();
            }
        });
        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewConfig viewConfig = options.getViewConfig();
        if (viewConfig != null) {
            int actionBarHeight = viewConfig.getActionBarHeight();
            if (actionBarHeight != 0) {
                mRLActionBar.getLayoutParams().height = actionBarHeight;
                ViewGroup.LayoutParams layoutParams = mIVBack.getLayoutParams();
                layoutParams.height = actionBarHeight;
                layoutParams.width = actionBarHeight;
            }
            if (viewConfig.getBackRes() != 0) {
                mIVBack.setImageResource(viewConfig.getBackRes());
            }
            if (viewConfig.getBackPadding() != null) {
                Rect backPadding = viewConfig.getBackPadding();
                mIVBack.setPadding(backPadding.left, backPadding.top, backPadding.right, backPadding.bottom);
            }
            if (viewConfig.getMenuHeight() != 0) {
                mEnsure.getLayoutParams().height = viewConfig.getMenuHeight();
            }
            if (viewConfig.getMenuBgRes() != 0) {
                mEnsure.setBackgroundResource(viewConfig.getMenuBgRes());
            }
            if (viewConfig.getMenuMarginRight() != -1) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mEnsure.getLayoutParams();
                marginLayoutParams.rightMargin = viewConfig.getMenuMarginRight();
            }
            if (!TextUtils.isEmpty(viewConfig.getTitle())) {
                ((TextView) findViewById(R.id.tv_title)).setText(viewConfig.getTitle());
            }
        }

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
        options = intent.getParcelableExtra(FileSelectOptions.OPTIONS_BEAN);
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
            bundle.putParcelable(FileSelectOptions.OPTIONS_BEAN, options);
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
