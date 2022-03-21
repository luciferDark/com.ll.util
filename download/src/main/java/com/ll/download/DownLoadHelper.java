package com.ll.download;

import com.ll.download.beans.DownloadBean;
import com.ll.download.configs.DownloadAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownLoadHelper {
    private Map<String, DownloadManager> downloadPond;
    private Map<String, DownloadBean> waitingPond;

    private String diskSavePath;
    private DownLoadHelper _instance;

    private int maxDownloadThread = 10;
    private int currentDownloadThread = 0;


    private DownLoadHelper() {
        init();
    }

    public DownLoadHelper instance() {
        synchronized (DownLoadHelper.class) {
            if (_instance == null) {
                synchronized (DownLoadHelper.class) {
                    _instance = new DownLoadHelper();
                }
            }
        }

        return _instance;
    }


    private void init() {
        downloadPond = new HashMap<>();
        waitingPond = new HashMap<>();
    }

    public DownLoadHelper setDiskSavePath(String diskSavePath) {
        this.diskSavePath = diskSavePath;
        return this;
    }

    public DownLoadHelper setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
        return this;
    }

    public void handleAction(int action, DownloadBean item, List<DownloadBean> list) {
        switch (action) {
            case DownloadAction.ACTION_START:
                start(item);
                break;
            case DownloadAction.ACTION_PAUSE:
                break;
            case DownloadAction.ACTION_RESUME:
                break;
            case DownloadAction.ACTION_RESTART:
                break;
            case DownloadAction.ACTION_DELETED:
                break;
            case DownloadAction.ACTION_START_ALL:
                break;
            case DownloadAction.ACTION_PAUSE_ALL:
                break;
            case DownloadAction.ACTION_RESUME_ALL:
                break;
            case DownloadAction.ACTION_DELETED_ALL:
                break;
        }
    }

    private void start(DownloadBean item) {
        currentDownloadThread = downloadPond.size();

        if (maxDownloadThread <= currentDownloadThread){
            //放到等待池中
            waitPond(item);
            return;
        }
    }

    private void waitPond(DownloadBean item) {
        if (!waitingPond.containsKey(item.getUrl()))
            waitingPond.put(item.getUrl(), item);
    }
}
