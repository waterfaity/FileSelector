package com.waterfairy.fileselector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/31 11:04
 * @info:
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private FileListBean mFileListBean;
    private File[] files;
    private Context mContext;

    public FileAdapter(Context mContext, FileListBean fileListBean) {
        this.mContext = mContext;
        setData(fileListBean);
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
                    onClickItemListener.onItemClick(pos, files[pos]);
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
            File file = files[filePos];
            if (file != null) {
                String name = file.getName();
                if (file.isDirectory()) {
                    //文件夹
                    holder.mIVIcon.setImageResource(R.mipmap.ic_folder);
                } else {
                    holder.mIVIcon.setImageResource(FileTypeUtils.getIcon(name));
                }
                //名字
                holder.mTVName.setText(name);
                String format = TimeUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss");
                holder.mTVInfo.setText(format);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mFileListBean == null) return 0;
        if (files == null) return 0;
        if (mFileListBean.getLevel() == 0) return files.length;
        else return files.length + 1;
    }

    public void setData(FileListBean fileListBean) {
        this.mFileListBean = fileListBean;
        if (fileListBean != null)
            files = fileListBean.getFileList(FileListBean.SORT_BY_TIME_DESC);
        else files = null;
    }

    public FileListBean getFileBeanData() {
        return mFileListBean;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIVIcon;
        private TextView mTVName, mTVInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            mIVIcon = itemView.findViewById(R.id.img);
            mTVName = itemView.findViewById(R.id.name);
            mTVInfo = itemView.findViewById(R.id.info);

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
