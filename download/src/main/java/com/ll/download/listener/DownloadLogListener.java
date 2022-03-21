package com.ll.download.listener;

import okhttp3.Interceptor;

public interface DownloadLogListener {
    void log(Interceptor.Chain chain);
}
