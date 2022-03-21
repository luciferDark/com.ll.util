package com.ll.download.listener;

import com.ll.download.beans.DownloadBean;

public interface DownloadProcessListener {
    void onProcess(DownloadBean item, long currentLength, long contentLength,
                   boolean isCompleted, double speed);
}
