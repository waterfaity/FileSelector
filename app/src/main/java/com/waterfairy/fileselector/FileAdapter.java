package com.waterfairy.fileselector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/31 11:04
 * @info: 文件展示adapter  ... ...
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener {
    private FileListBean mFileListBean;
    private File[] mDataList;
    private Context mContext;
    private HashMap<String, File> mSelectFiles;
    private int maxNum = -1;
    private boolean canSelect = true;//文件选择
    private boolean canSelectDir;//文件夹选择
    private boolean canOnlySelectCurrentDir = true;//只能选择当前文件夹的文件


    public FileAdapter(Context mContext, FileListBean fileListBean) {
        this.mContext = mContext;
        setData(fileListBean);
    }

    public void setCanSelect(boolean canSelect, int maxNum) {
        this.canSelect = canSelect;
        this.maxNum = maxNum;
        if (canSelect) {
            mSelectFiles = new HashMap<>();
        } else {
            if (mSelectFiles != null) {
                mSelectFiles.clear();
                mSelectFiles = null;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_file_select, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener == null) return;
                Object tag = v.getTag();
                if (tag == null) {
                    onClickItemListener.onBackClick();
                } else {
                    int pos = (int) tag;
                    onClickItemListener.onItemClick(pos, mDataList[pos]);
                }
            }
        });

        boolean jumpZero = false;
        int filePos = 0;
        if (mFileListBean.getLevel() == 0) {
            filePos = position;
            holder.itemView.setTag(filePos);
        } else {
            filePos = position - 1;
            holder.itemView.setTag(filePos);
            if (position == 0) {
                //第一次项 返回上一级
                holder.mIVIcon.setImageResource(R.mipmap.ic_folder);
                holder.mTVName.setText("..");
                holder.mTVInfo.setText("返回上一级");
                jumpZero = true;
                holder.itemView.setTag(null);
            }
        }

        if (!jumpZero) {
            File file = mDataList[filePos];
            if (file != null) {
                String name = file.getName();
                if (file.isDirectory()) {
                    //文件夹
                    holder.mIVIcon.setImageResource(R.mipmap.ic_folder);
                } else {
                    int resId = FileUtils.getIcon(name);
                    if (resId == R.mipmap.ic_img)
                        Glide.with(mContext).load(file).into(holder.mIVIcon);
                    else
                        holder.mIVIcon.setImageResource(FileUtils.getIcon(name));
                }
                //名字
                holder.mTVName.setText(name);
                String format = TimeUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss");
                if (file.isFile()) {
                    String fileLen = FileUtils.getFileLen(file);
                    holder.mTVInfo.setText(format + "　" + fileLen);
                } else {
                    holder.mTVInfo.setText(format);
                }
            }
            //
            if (!canSelect || (file.isDirectory() && !canSelectDir)) {
                holder.mCheckBox.setVisibility(View.GONE);
                holder.mCheckBox.setOnCheckedChangeListener(null);
            } else {
                holder.mCheckBox.setTag(filePos);
                holder.mCheckBox.setVisibility(View.VISIBLE);
                holder.mCheckBox.setOnCheckedChangeListener(null);
                if (mSelectFiles != null) {
                    holder.mCheckBox.setChecked(mSelectFiles.containsKey(file.getAbsolutePath()));
                }
                holder.mCheckBox.setOnCheckedChangeListener(this);
            }

        } else {
            holder.mCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mFileListBean == null) return 0;
        if (mDataList == null) return 0;
        if (mFileListBean.getLevel() == 0) return mDataList.length;
        else return mDataList.length + 1;
    }

    public void setData(FileListBean fileListBean) {
        this.mFileListBean = fileListBean;
        if (fileListBean != null)
            mDataList = fileListBean.getFileList(FileListBean.SORT_BY_NAME);
        else mDataList = null;
        if (canOnlySelectCurrentDir) removeOtherDirSelectFiles();
    }

    public FileListBean getFileBeanData() {
        return mFileListBean;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();
        if (tag == null) return;
        int pos = (int) tag;
        File file = mDataList[pos];
        if (file != null) {
            if (isChecked) {
                //添加
                if (maxNum > -1 && mSelectFiles.size() >= maxNum) {
                    //不再添加
                    CheckBox checkBox = (CheckBox) buttonView;
                    checkBox.setOnCheckedChangeListener(null);
                    checkBox.setChecked(false);
                    checkBox.setOnCheckedChangeListener(this);
                    return;
                }
                mSelectFiles.put(file.getAbsolutePath(), file);

            } else {
                //移除
                mSelectFiles.remove(file.getAbsolutePath());
            }
        }
    }

    public void setCanSelectDir(boolean canSelectDir) {

        this.canSelectDir = canSelectDir;
        //设置为 false 有选中文件且数量不为0
        if (mSelectFiles != null && !canSelectDir && mSelectFiles.size() > 0) {
            Set<String> strings = mSelectFiles.keySet();
            Iterator<String> iterator = strings.iterator();
            List<String> removeList = null;
            while (iterator.hasNext()) {
                String key = iterator.next();
                File file = mSelectFiles.get(key);
                if (file.isDirectory()) {
                    //有文件夹被选择
                    if (removeList == null) {
                        removeList = new ArrayList<>();
                    }
                    removeList.add(file.getAbsolutePath());
                }
            }
            if (removeList != null) {
                //移除所有的选中文件夹
                for (int i = 0; i < removeList.size(); i++) {
                    mSelectFiles.remove(removeList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 获取选中的文件
     *
     * @return
     */
    public HashMap<String, File> getSelectFiles() {
        return mSelectFiles;
    }

    /**
     * 移除所有文件
     */
    public void removeAllFiles() {
        if (mSelectFiles != null) {
            mSelectFiles.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 移除当前文件夹所有文件
     */
    public void removeCurrentDirAllFiles() {
        if (mSelectFiles == null) return;
        if (mDataList == null) return;
        for (File file : mDataList) {
            mSelectFiles.remove(file.getAbsolutePath());
        }
        notifyDataSetChanged();
    }

    /**
     * 添加当前文件夹所有文件
     */
    public void selectAllFiles() {
        if (mDataList == null || !canSelect) return;
        if (mSelectFiles == null) mSelectFiles = new HashMap<>();
        for (File file : mDataList) {
            if (file.isDirectory() && !canSelectDir) continue;
            mSelectFiles.put(file.getAbsolutePath(), file);
        }
        notifyDataSetChanged();
    }

    public void setCanOnlySelectCurrentDir(boolean canOnlySelectCurrentDir) {
        this.canOnlySelectCurrentDir = canOnlySelectCurrentDir;
        removeOtherDirSelectFiles();
    }

    /**
     * 移除非本文件夹的文件
     * 本方法 不需要刷新
     */
    private void removeOtherDirSelectFiles() {
        if (mSelectFiles != null && mSelectFiles.size() > 0 && mDataList != null && mDataList.length > 0) {
            Set<String> strings = mSelectFiles.keySet();
            Iterator<String> iterator = strings.iterator();
            String parentFile = mFileListBean.getFile().getAbsolutePath();
            List<String> removeList = null;
            while (iterator.hasNext()) {
                String next = iterator.next();
                File file = mSelectFiles.get(next);
                //选中的文件的父类 != 父类文件(file)  则移除
                if (!TextUtils.equals(file.getParentFile().getAbsolutePath(), parentFile)) {
                    if (removeList == null) removeList = new ArrayList<>();
                    removeList.add(next);
                }
            }
            if (removeList != null) {
                for (int i = 0; i < removeList.size(); i++) {
                    mSelectFiles.remove(removeList.get(i));
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIVIcon;
        private TextView mTVName, mTVInfo;
        private CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mIVIcon = itemView.findViewById(R.id.img);
            mTVName = itemView.findViewById(R.id.name);
            mTVInfo = itemView.findViewById(R.id.info);
            mCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }

    private OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        /**
         * @param pos  位置
         * @param file 文件
         */
        void onItemClick(int pos, File file);

        void onBackClick();
    }
}
