package com.waterfairy.fileselector;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019-11-21 10:57
 * @info:
 */
public class FileSearchConfig {


    private String[] extensions;
    private String[] ignorePaths;
    private String[] searchPaths;
    private String[] projections;
    private String sortOrder;//MediaStore.Images.Media.DATE_MODIFIED
    private Uri contentUri;


    public static FileSearchConfig defaultInstance() {
        FileSearchConfig fileSearchConfig = new FileSearchConfig();
        fileSearchConfig.setContentUri(MediaStore.Files.getContentUri("external"));
        fileSearchConfig.setSortOrder(MediaStore.Files.FileColumns.DATE_MODIFIED);
        fileSearchConfig.setProjections(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE);
        return fileSearchConfig;
    }

    public FileSearchConfig setExtensions(String... extensions) {
        this.extensions = extensions;
        return this;
    }

    public FileSearchConfig setIgnorePaths(String... ignorePaths) {
        this.ignorePaths = ignorePaths;
        return this;
    }

    public FileSearchConfig setSearchPaths(String... searchPaths) {
        this.searchPaths = searchPaths;
        return this;
    }

    public FileSearchConfig setProjections(String... projections) {
        this.projections = projections;
        return this;
    }

    public FileSearchConfig setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }


    public String[] getExtensions() {
        return extensions;
    }

    public String[] getIgnorePaths() {
        return ignorePaths;
    }

    public String[] getSearchPaths() {
        return searchPaths;
    }

    public String[] getProjections() {
        return projections;
    }

    public String getSortOrder() {
        return sortOrder;
    }


    public Uri getContentUri() {
        return contentUri;
    }

    public FileSearchConfig setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
        return this;
    }
}
