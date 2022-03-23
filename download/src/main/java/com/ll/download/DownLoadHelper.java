package com.ll.download;

import com.ll.download.beans.DownloadBean;
import com.ll.download.configs.DownloadAction;
import com.ll.download.listener.DownloadListener;
import com.ll.download.listener.DownloadLogListener;
import okhttp3.Interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownLoadHelper {
    private Map<String, DownloadManager> downloadPond;
    private Map<String, DownloadBean> waitingPond;

    private String diskSavePath;
    private DownLoadHelper _instance;
    private DownloadListener defaultDownLoadListener;
    private DownloadListener downLoadListener;
    private DownloadLogListener downloadLogListener;
    private DownloadLogListener defaultDownloadLogListener;

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

    public DownLoadHelper setDownLoadListener(DownloadListener downLoadListener) {
        this.downLoadListener = downLoadListener;
        return this;
    }

    public DownLoadHelper setDownloadLogListener(DownloadLogListener downloadLogListener) {
        this.downloadLogListener = downloadLogListener;
        return this;
    }

    private void init() {
        downloadPond = new HashMap<>();
        waitingPond = new HashMap<>();

        defaultDownLoadListener = new DownloadListener() {
            @Override
            public void onStart(DownloadBean item) {

            }

            @Override
            public void onPause(DownloadBean item) {

            }

            @Override
            public void onResume(DownloadBean item) {

            }

            @Override
            public void onCompleted(DownloadBean item) {

            }

            @Override
            public void onWaiting(DownloadBean item) {

            }

            @Override
            public void onError(DownloadBean item, int errorCode, String msg) {

            }

            @Override
            public void onDeleted(DownloadBean item) {

            }

            @Override
            public void onProcess(DownloadBean item, long currentLength, long contentLength, boolean isCompleted, double speed) {

            }
        };
        defaultDownloadLogListener = new DownloadLogListener() {
            @Override
            public void log(Interceptor.Chain chain) {

            }
        };

        downLoadListener = defaultDownLoadListener;
        downloadLogListener = defaultDownloadLogListener;
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
        DownloadManager downloadManager = null;
        if (downloadPond.containsKey(item.getUrl())){
            downloadManager = downloadPond.get(item.getUrl());
        } else {
            downloadManager = new DownloadManager(item);
            downloadPond.put(item.getUrl(),downloadManager);
        }

        downloadManager.setListener(downLoadListener);
        downloadManager.setLogListener(downloadLogListener);

        downloadManager.start();

        currentDownloadThread ++;
    }

    private void waitPond(DownloadBean item) {
        if (!waitingPond.containsKey(item.getUrl()))
            waitingPond.put(item.getUrl(), item);
    }
}
