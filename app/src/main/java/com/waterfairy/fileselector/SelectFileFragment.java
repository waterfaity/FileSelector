package com.waterfairy.fileselector;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
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
public class SelectFileFragment extends Fragment implements FileAdapter.OnClickItemListener {
    private RecyclerView mRecyclerView;
    private View mRootView;
    private int currentLevel;//0 == sdcard
    private HashMap<Integer, FileListBean> fileHashMap;
    private FileAdapter mAdapter;
    private TextView mTVPath;
    private boolean canSelect = true;//是否可以选择文件
    private boolean canSelectDir;//是否可以选择文件夹
    private int limitNum = -1;
    private boolean canOnlySelectCurrentDir = true;//只能选择当前文件夹

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
        initView();
        initData();
    }

    private void initData() {
        fileHashMap = new HashMap<>();
        String externalStorageState = Environment.getExternalStorageState();
        if (TextUtils.equals(Environment.MEDIA_MOUNTED, externalStorageState)) {
            queryFile(Environment.getExternalStorageDirectory(), 0);
        } else {
            ToastShowTool.show("未挂载存储卡");
        }
    }

    /**
     * 查询文件list
     *
     * @param file
     * @param level
     */
    private void queryFile(File file, int level) {
        if (file == null) {
            return;
        }
        FileListBean fileListBean = new FileListBean();
        fileListBean.setFile(file);
        fileListBean.setLevel(level);
        if (file.exists()) {
            fileListBean.setFileList(file.listFiles());
        } else {
            ToastShowTool.show("文件不存在");
        }

        currentLevel = level;
        fileHashMap.put(level, fileListBean);
        showFileList(fileListBean);
    }

    /**
     * 展示
     *
     * @param fileListBean
     */
    private void showFileList(FileListBean fileListBean) {
        mTVPath.setText(fileListBean.getFile().getAbsolutePath());
        if (mAdapter == null) {
            mAdapter = new FileAdapter(getActivity(), fileListBean);
            mAdapter.setCanSelect(canSelect, limitNum);
            mAdapter.setCanSelectDir(canSelectDir);
            mAdapter.setCanOnlySelectCurrentDir(canOnlySelectCurrentDir);
            mAdapter.setOnClickItemListener(this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(fileListBean);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mTVPath = findViewById(R.id.path);
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
        if (currentLevel == 0) return;
        showFileList(fileHashMap.get(--currentLevel));
    }

    @Override
    public void onItemClick(int pos, File file) {
        if (file.isDirectory())
            queryFile(file, currentLevel + 1);
        else ToastShowTool.show("文件:" + file.getName());
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
        setCanSelect(canSelect, SelectFileActivity.NO_LIMIT);
    }

    /**
     * @param canSelect
     * @param limitNum  最大值
     */
    public void setCanSelect(boolean canSelect, int limitNum) {
        this.canSelect = canSelect;
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
        this.canSelectDir = canSelectDir;
        if (mAdapter != null) {
            mAdapter.setCanSelectDir(canSelectDir);
        }
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
        this.canOnlySelectCurrentDir = canOnlySelectCurrentDir;
        if (mAdapter != null) {
            mAdapter.setCanOnlySelectCurrentDir(canOnlySelectCurrentDir);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ToastShowTool.initToast(getActivity());
    }
}
