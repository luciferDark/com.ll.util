package com.ll.download.beans;

import com.ll.download.apis.DownloadRetrofitApi;

import java.io.File;

public class DownloadBean {
    private int id = -1;
    private String url;
    private String fileName;
    private String diskPath;
    private long currentLength = 0;
    private long contentLength = 0;
    private boolean isCompleted = false;

    protected DownloadRetrofitApi downloadRetrofitApi;
    public DownloadBean() {
    }

    public DownloadBean(String url, String fileName, String diskPath) {
        this.url = url;
        this.fileName = fileName;
        this.diskPath = diskPath;
    }

    public DownloadBean(String url, String fileName, String diskPath, long currentLength, long contentLength, boolean isCompleted) {
        this.url = url;
        this.fileName = fileName;
        this.diskPath = diskPath;
        this.currentLength = currentLength;
        this.contentLength = contentLength;
        this.isCompleted = isCompleted;
    }

    public DownloadBean(int id, String url, String fileName, String diskPath, long currentLength, long contentLength, boolean isCompleted) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.diskPath = diskPath;
        this.currentLength = currentLength;
        this.contentLength = contentLength;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public DownloadBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DownloadBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public DownloadBean setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public String getDiskFullPath() {
        String fixStr = "";
        if (getDiskPath().endsWith("/")){
            fixStr = "";
        } else {
            fixStr = File.separator;
        }
        return getDiskPath() + fixStr + getFileName();
    }

    public DownloadBean setDiskPath(String diskPath) {
        this.diskPath = diskPath;
        return this;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public DownloadBean setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
        return this;
    }

    public long getContentLength() {
        return contentLength;
    }

    public DownloadBean setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public DownloadBean setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public DownloadRetrofitApi getDownloadRetrofitApi() {
        return downloadRetrofitApi;
    }

    public DownloadBean setDownloadRetrofitApi(DownloadRetrofitApi downloadRetrofitApi) {
        this.downloadRetrofitApi = downloadRetrofitApi;
        return this;
    }
}
