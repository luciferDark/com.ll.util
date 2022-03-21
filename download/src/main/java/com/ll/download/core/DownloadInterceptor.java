package com.ll.download.core;

import com.ll.download.beans.DownloadBean;
import com.ll.download.listener.DownloadLogListener;
import com.ll.download.listener.DownloadProcessListener;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class DownloadInterceptor implements Interceptor {
    private DownloadBean item;
    private DownloadProcessListener processListener;
    private DownloadLogListener logListener;

    public DownloadInterceptor() {
    }

    public DownloadInterceptor(DownloadBean item, DownloadProcessListener listener) {
        this.item = item;
        this.processListener = listener;
    }

    public DownloadInterceptor setItem(DownloadBean item) {
        this.item = item;
        return this;
    }

    public DownloadInterceptor setProcessListener(DownloadProcessListener listener) {
        this.processListener = listener;
        return this;
    }

    public DownloadInterceptor setLogListener(DownloadLogListener logListener) {
        this.logListener = logListener;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (null != logListener){
            logListener.log(chain);
        }
        Response newResponse = response.newBuilder().body(
                new DownloadResponseBody(item, response.body(), processListener)
        ).build();

        return newResponse;
    }
}
