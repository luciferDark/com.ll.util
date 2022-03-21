package com.ll.download.listener;

import com.ll.download.beans.DownloadBean;

public interface DownloadListener extends DownloadProcessListener {
    void onStart(DownloadBean item);

    void onPause(DownloadBean item);

    void onResume(DownloadBean item);

    void onCompleted(DownloadBean item);

    void onWaiting(DownloadBean item);

    void onError(DownloadBean item, int errorCode, String msg);

    void onDeleted(DownloadBean item);
}
