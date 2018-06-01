package com.waterfairy.fileselector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public FileAdapter(FileListBean fileListBean, Context mContext) {
        this.mFileListBean = fileListBean;
        if (fileListBean != null)
            files = fileListBean.getFileList();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_file_select, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if (files == null) return 1;
        return files.length + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnClickItemListener {
        void onItemClick(int pos, File file);
    }
}
