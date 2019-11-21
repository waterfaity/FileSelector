package com.waterfairy.fileselector;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static android.view.View.NO_ID;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/30 18:49
 * @info:
 */
public class FileSelectFragment extends Fragment implements FileAdapter.OnClickItemListener, FileQueryTool.OnFileQueryListener {
    private RecyclerView mRecyclerView;
    private View mRootView;
    private FileAdapter mAdapter;
    private TextView mTVPath;
    private HorizontalScrollView mHorScrollView;
    private FileQueryTool fileQueryTool;//文件查询工具
    private FileSelectOptions options;
    private OnFileSelectListener onFileSelectListener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getExtra();
        findView();
        initView();
        initData();
    }

    private void getExtra() {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(FileSelectOptions.OPTIONS_BEAN)) {
            options = (FileSelectOptions) arguments.getSerializable(FileSelectOptions.OPTIONS_BEAN);
        }
    }

    private void initData() {
        initFileQueryTool();
        String externalStorageState = Environment.getExternalStorageState();
        if (TextUtils.equals(Environment.MEDIA_MOUNTED, externalStorageState)) {
            fileQueryTool.queryFile(Environment.getExternalStorageDirectory(), 0);
        } else {
            ToastShowTool.show("未挂载存储卡");
        }
    }

    private void initFileQueryTool() {
        if (fileQueryTool == null) {
            fileQueryTool = new FileQueryTool();
            fileQueryTool.setSearchHiddenFile(getOptions().isShowHiddenFile());
            fileQueryTool.setSelectType(getOptions().getSelectType());
            fileQueryTool.setOnFileQueryListener(this);
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mTVPath = findViewById(R.id.path);
        mHorScrollView = findViewById(R.id.hor_scroll_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return mRootView = inflater.inflate(R.layout.fragment_select_file, container, false);
    }

    @Nullable
    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == NO_ID) {
            return null;
        }
        return mRootView.findViewById(id);
    }


    public void back() {
        fileQueryTool.back();
    }

    @Override
    public void onItemClick(int pos, File file) {
        if (file.isDirectory())
            fileQueryTool.queryFileNext(file);
        else {
            if (getActivity() != null) {
//                ToastShowTool.show("文件:" + file.getName());
                try {
                    ProviderUtils.setAuthority(options.getPathAuthority());
                    FileUtils.openFile(getActivity(), file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 返回上一级
     */
    @Override
    public void onBackClick() {
        back();
    }

    /**
     * 是否可以选择
     *
     * @param canSelect
     */
    public void setCanSelect(boolean canSelect) {
        setCanSelect(canSelect, FileSelectActivity.NO_LIMIT);
    }

    /**
     * @param canSelect
     * @param limitNum  最大值
     */
    public void setCanSelect(boolean canSelect, int limitNum) {
        if (mAdapter != null) {
            mAdapter.setCanSelect(canSelect, limitNum);
        }
    }

    /**
     * 文件夹是否可以选择
     *
     * @param canSelectDir
     */
    public void setCanSelectDir(boolean canSelectDir) {
        if (mAdapter != null) {
            mAdapter.setCanSelectDir(canSelectDir);
        }
    }

    public void setSelectType(String selectType) {
        initFileQueryTool();
        fileQueryTool.setSelectType(selectType);
    }

    /**
     * 获取选中的所有文件
     *
     * @return
     */
    public ArrayList<File> getSelectFileList() {
        HashMap<String, File> fileHashMap = getSelectFiles();

        ArrayList<File> fileList = null;
        if (fileHashMap != null) {
            Set<String> strings = fileHashMap.keySet();
            for (String next : strings) {
                File file = fileHashMap.get(next);
                if (fileList == null)
                    fileList = new ArrayList<>();
                fileList.add(file);
            }
        }
        return fileList;
    }


    public HashMap<String, File> getSelectFiles() {
        if (mAdapter != null) {
            return mAdapter.getSelectFiles();
        }
        return null;
    }

    /**
     * 移除所有选中的文件
     */
    public void removeAll() {
        if (mAdapter != null) {
            mAdapter.removeAllFiles();
        }
    }

    /**
     * 移除当前文件夹的所有文件
     */
    public void removeCurrentDirAllFiles() {
        if (mAdapter != null) {
            mAdapter.removeCurrentDirAllFiles();
        }
    }

    /**
     * 选中所有的files
     */
    public void selectAllFiles() {
        if (mAdapter != null) {
            mAdapter.selectAllFiles();
        }
    }

    /**
     * 只能选择当前文件夹的文件
     *
     * @param canOnlySelectCurrentDir
     */
    public void setCanOnlySelectCurrentDir(boolean canOnlySelectCurrentDir) {
        if (mAdapter != null) {
            mAdapter.setCanOnlySelectCurrentDir(canOnlySelectCurrentDir);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ToastShowTool.initToast(getActivity());
    }

    /**
     * @param fileListBean
     */
    @Override
    public void onQueryFile(FileListBean fileListBean) {
        mTVPath.setText(fileListBean.getFile().getAbsolutePath());
        if (mAdapter == null) {
            mAdapter = new FileAdapter(getActivity(), fileListBean);
            mAdapter.setCanSelect(getOptions().isCanSelect(), getOptions().getLimitNum());
            mAdapter.setCanSelectDir(getOptions().isCanSelectDir());
            mAdapter.setCanOnlySelectCurrentDir(getOptions().isCanOnlySelectCurrentDir());
            mAdapter.setOnClickItemListener(this);
            mAdapter.setOnFileSelectListener(onFileSelectListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(fileListBean);
            mAdapter.notifyDataSetChanged();
        }

        scrollPath();
    }

    private void scrollPath() {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mHorScrollView.scrollTo(mTVPath.getMeasuredWidth() - mHorScrollView.getWidth(), 0);
            }
        }.sendEmptyMessageDelayed(0, 100);
    }

    private FileSelectOptions getOptions() {
        if (options == null) {
            options = new FileSelectOptions();
        }
        return options;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return fileQueryTool.back();
        }
        return false;
    }

    public void setOnFileSelectListener(OnFileSelectListener onFileSelectListener) {
        this.onFileSelectListener = onFileSelectListener;
    }

    public OnFileSelectListener getOnFileSelectListener() {
        return onFileSelectListener;
    }
}
