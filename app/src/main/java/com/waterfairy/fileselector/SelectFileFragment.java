package com.waterfairy.fileselector;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waterfairy.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import static android.view.View.NO_ID;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/30 18:49
 * @info:
 */
public class SelectFileFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private View mRootView;
    private int currentLevel;//0 == sdcard
    private HashMap<Integer, FileListBean> fileHashMap;

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
            ToastUtils.show("未挂载存储卡");
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
        if (file.exists()) {
            fileListBean.setFileList(file.listFiles());
        } else {
            ToastUtils.show("文件不存在");
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

    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recycler_view);
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
}
