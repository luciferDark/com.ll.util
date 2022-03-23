package com.ll.download;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ll.download.apis.DownloadRetrofitApi;
import com.ll.download.beans.DownloadBean;
import com.ll.download.configs.DownloadError;
import com.ll.download.configs.DownloadStatus;
import com.ll.download.core.DownloadInterceptor;
import com.ll.download.core.DownloadResponseBody;
import com.ll.download.listener.DownloadListener;
import com.ll.download.listener.DownloadLogListener;
import com.ll.download.listener.DownloadProcessListener;
import com.ll.download.util.FileUtil;
import com.ll.download.util.Log;
import com.ll.download.util.TextUtil;
import com.ll.download.util.UrlUtils;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

public class DownloadManager implements DownloadProcessListener {
    protected DownloadBean downloadItem;
    protected DownloadListener listener;
    protected DownloadLogListener logListener;
    protected DownloadRetrofitApi downloadRetrofitApi;

    private Disposable disposable;
    private int mDownloadStatus = DownloadStatus.DEFAULT;

    public DownloadManager setDownloadItem(DownloadBean downloadItem) {
        this.downloadItem = downloadItem;
        return this;
    }

    public DownloadManager setListener(DownloadListener listener) {
        this.listener = listener;
        return this;
    }
    public DownloadManager setLogListener(DownloadLogListener logListener) {
        this.logListener = logListener;
        return this;
    }

    public DownloadManager setDownloadRetrofitApi(DownloadRetrofitApi downloadRetrofitApi) {
        this.downloadRetrofitApi = downloadRetrofitApi;
        return this;
    }

    public DownloadManager(DownloadBean downloadItem) {
        setDownloadItem(downloadItem);
    }

    public void start() {
        if (null == downloadItem || TextUtil.empty(downloadItem.getUrl())) {
            Log.log("任务信息异常...");
            onDownloadError(DownloadError.Error_DownLoadInfoError,null);
            return;
        }

        if (mDownloadStatus == DownloadStatus.DOWNLOADING) {
            Log.log("任务正在下载中...", downloadItem.getUrl());
            onDownloadError(DownloadError.Error_DownLoadDuplicate,null);
            return;
        }

        if (mDownloadStatus == DownloadStatus.PAUSE){
            Log.log("一个暂停的任务，调用了start");
            resume();
            return;
        }

        if (downloadItem.isCompleted()) {
            mDownloadStatus = DownloadStatus.COMPLETED;
            if (null != listener) {
                listener.onCompleted(downloadItem);
            }
            return;
        }

        DownloadInterceptor interceptor = new DownloadInterceptor();
        interceptor.setItem(downloadItem)
                .setProcessListener(listener)
                .setLogListener(logListener);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor);
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(UrlUtils.getBasUrl(downloadItem.getUrl()))
                .build();
        if (downloadRetrofitApi == null) {
            downloadRetrofitApi = retrofit.create(DownloadRetrofitApi.class);
        }
        downloadItem.setDownloadRetrofitApi(downloadRetrofitApi);

        startDownload();
    }

    private void startDownload() {
        downloadRetrofitApi.download(
                downloadItem.getUrl())
                .map(responseBody -> {
                    downloadItem.setContentLength(responseBody.contentLength());
                    FileUtil.writeToDisk(responseBody, downloadItem);
                    return downloadItem;
                })
                .subscribeWith(new Observer<DownloadBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.log("onSubscribe");
                        disposable = d;
                        mDownloadStatus = DownloadStatus.START;
                    }


                    @Override
                    public void onNext(@NonNull DownloadBean body) {
                        Log.log("onNext");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.log("onError", e.getLocalizedMessage());
                        mDownloadStatus = DownloadStatus.ERROR;
                        onDownloadError(mDownloadStatus,e.getLocalizedMessage());

                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.log("onComplete");
                        mDownloadStatus = DownloadStatus.COMPLETED;
                        downloadItem.setCompleted(true);
                        if (null != listener)listener.onCompleted(downloadItem);

                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }
                });
    }

    public void pause() {
        if (mDownloadStatus == DownloadStatus.PAUSE){
            Log.log("任务已经暂停");
            return;
        }

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            mDownloadStatus = DownloadStatus.PAUSE;
            if (null != listener) listener.onPause(downloadItem);
        }
    }

    public void resume() {
        if (mDownloadStatus != DownloadStatus.PAUSE){
            Log.log("不是个暂停任务，无法resume");
            return;
        }

        mDownloadStatus = DownloadStatus.RESUME;
        if (null != listener) listener.onResume(downloadItem);
        startDownload();
    }

    public void delete() {
        if (mDownloadStatus != DownloadStatus.DELETED){
            Log.log("任务删除中，请稍后");
            return;
        }

        pause();
        FileUtil.deletFromDisk(downloadItem.getDiskFullPath());
        mDownloadStatus = DownloadStatus.DELETED;
        if (null != listener) listener.onDeleted(downloadItem);
    }

    @Override
    public void onProcess(DownloadBean item, long currentLength,
                          long contentLength, boolean isCompleted, double speed) {
        if ( mDownloadStatus != DownloadStatus.DOWNLOADING)
            mDownloadStatus = DownloadStatus.DOWNLOADING;
        downloadItem.setCurrentLength(currentLength)
                .setCompleted(isCompleted);
        if (null != listener) listener.onProcess(downloadItem,currentLength,contentLength,isCompleted,speed);
    }

    public void onDownloadError(int code, String msg) {
        if (null == msg) msg = "";
        if (null != listener) {
            listener.onError(downloadItem, code, DownloadError.getMsg(code) + msg);
        }
    }
}
