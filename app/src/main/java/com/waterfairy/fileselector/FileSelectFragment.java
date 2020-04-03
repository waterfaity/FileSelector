package com.waterfairy.fileselector;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class FileSelectFragment extends Fragment implements FileAdapter.OnClickItemListener, FileQueryTool.OnFileQueryListener, FileSearchTool2.OnFileQueryListener {
    private RecyclerView mRecyclerView;
    private View mRootView;
    private FileAdapter mAdapter;
    private TextView mTVPath;
    private HorizontalScrollView mHorScrollView;
    private FileQueryTool fileQueryTool;//文件查询工具
    private FileSearchTool2 fileQueryTool2;//文件查询工具
    private FileSelectOptions options;
    private OnFileSelectListener onFileSelectListener;
    private FileSelectLoadingDialog fileSelectLoadingDialog;

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
            if (options.getSearchStyle() == FileSelectOptions.STYLE_FOLDER_AND_FILE)
                fileQueryTool.queryFile(Environment.getExternalStorageDirectory(), 0);
            else {
                fileSelectLoadingDialog = new FileSelectLoadingDialog(getContext());
                fileSelectLoadingDialog.show();
                fileQueryTool2.start();
            }
        } else {
            ToastShowTool.show("未挂载存储卡");
        }
    }

    private void initFileQueryTool() {
        if (options.getSearchStyle() == FileSelectOptions.STYLE_FOLDER_AND_FILE) {
            if (fileQueryTool == null) {
                fileQueryTool = new FileQueryTool();
                fileQueryTool.setSearchStyle(options.getSearchStyle());
                fileQueryTool.setSearchHiddenFile(getOptions().isShowHiddenFile());
                fileQueryTool.setSelectType(getOptions().getSelectType());
                fileQueryTool.setOnFileQueryListener(this);
            }
        } else {
            if (fileQueryTool2 == null) {
                fileQueryTool2 = FileSearchTool2.newInstance(getContext());
                fileQueryTool2.setOnSearchListener(this);
                FileSearchConfig fileSearchConfig = FileSearchConfig.defaultInstance();
                fileSearchConfig.setExtensions(getOptions().getExtensions());
                fileSearchConfig.setIgnorePaths(getOptions().getIgnorePaths());
                fileQueryTool2.setConfig(fileSearchConfig);
            }
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getOptions().getSearchStyle() == FileSelectOptions.STYLE_ONLY_FILE) {
            mTVPath.setVisibility(View.GONE);
        }
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
        if (fileQueryTool != null)
            fileQueryTool.back();
    }

    @Override
    public void onItemClick(int pos, File file) {
        if (file.isDirectory()) {
            if (fileQueryTool != null)
                fileQueryTool.queryFileNext(file);
        } else {
            if (options.isCanOpenFile()) {
                if (getActivity() != null) {
                    try {
                        ProviderUtils.setAuthority(options.getPathAuthority());
                        FileUtils.openFile(getActivity(), file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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

    /**
     * 获取选中的所有文件
     *
     * @return
     */
    public ArrayList<String> getSelectFilePathList() {
        HashMap<String, File> fileHashMap = getSelectFiles();

        ArrayList<String> fileList = null;
        if (fileHashMap != null) {
            Set<String> strings = fileHashMap.keySet();
            for (String next : strings) {
                File file = fileHashMap.get(next);
                if (fileList == null)
                    fileList = new ArrayList<>();
                fileList.add(file.getAbsolutePath());
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
            mAdapter.setSortType(getOptions().getSortType());
            mAdapter.setData(fileListBean);
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
            if (fileQueryTool != null) {
                return fileQueryTool.back();
            }
        }
        return false;
    }

    public void setOnFileSelectListener(OnFileSelectListener onFileSelectListener) {
        this.onFileSelectListener = onFileSelectListener;
    }

    public OnFileSelectListener getOnFileSelectListener() {
        return onFileSelectListener;
    }

    /**
     * search 2
     *
     * @param file
     */
    @Override
    public void onSearch(File file) {

    }

    /**
     * search 2
     *
     * @param fileArrayList
     */
    @Override
    public void onSearchSuccess(ArrayList<File> fileArrayList) {
        if (fileSelectLoadingDialog != null)
            fileSelectLoadingDialog.dismiss();
        FileListBean fileListBean = new FileListBean();
        fileListBean.setLevel(0);
        fileListBean.setFile(Environment.getExternalStorageDirectory());
        File[] files = new File[fileArrayList.size()];
        for (int i = 0; i < fileArrayList.size(); i++) {
            files[i] = fileArrayList.get(i);
        }
        fileListBean.setFileList(files);
        onQueryFile(fileListBean);
    }

}
