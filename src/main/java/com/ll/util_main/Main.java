package com.ll.util_main;

import com.ll.download.DownloadManager;
import com.ll.download.beans.DownloadBean;
import com.ll.download.listener.DownloadListener;
import com.ll.download.listener.DownloadLogListener;
import com.ll.download.util.SpeedUtil;
import com.ll.util_main.test.RxTest;
import com.ll.utils.Log;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

import java.text.DecimalFormat;

public class Main {

    public static void main(String[] args) {
        Log.log("main start");
//        test();
        test1();
    }

    private static void test1() {
//        String url = "https://www.baidu.com/img/superlogo_c4d7df0a003d3db9b65e9ef0fe6da1ec.png";
        String url = "https://onsooo.uber98.com/20210816/5751/3.jpg?x-oss-process=image/resize,w_1280";
        String diskPath = "d:/downloadTest";
        String fileName = "1.jpg";
        DownloadBean bean = new DownloadBean();
        bean.setUrl(url)
                .setDiskPath(diskPath)
                .setFileName(fileName);

        DownloadManager dm = new DownloadManager(bean);
        dm.setListener(new DownloadListener() {
            @Override
            public void onStart(DownloadBean item) {
                Log.log("onStart", item.getUrl());
            }

            @Override
            public void onPause(DownloadBean item) {
                Log.log("onPause", item.getUrl());
            }

            @Override
            public void onResume(DownloadBean item) {
                Log.log("onResume", item.getUrl());
            }

            @Override
            public void onCompleted(DownloadBean item) {
                Log.log("onCompleted", item.getUrl());
                System.exit(0);
            }

            @Override
            public void onWaiting(DownloadBean item) {
                Log.log("onWaiting", item.getUrl());
            }

            @Override
            public void onError(DownloadBean item, int errorCode, String msg) {
                Log.log("onError", errorCode, msg, item.getUrl());
            }

            @Override
            public void onDeleted(DownloadBean item) {
                Log.log("onDeleted", item.getUrl());
            }

            @Override
            public void onProcess(DownloadBean item, long currentLength, long contentLength, boolean isCompleted, double speed) {
                DecimalFormat df = new DecimalFormat("0.00");
                Log.log("onProcess url:" + item.getDiskFullPath() +
                                "总长度", contentLength,
                        "当前进大小", currentLength,
                        "当前进度", df.format(((double) currentLength) / contentLength),
                        "是否完成", isCompleted,
                        "速度", SpeedUtil.FormatSize(speed));
            }
        });

        dm.setLogListener(chain -> {
            HttpUrl urlCall = chain.request().url();
            Log.log("Interceptor call url", urlCall.toString());
        });

        dm.start();
    }

    private static void test() {
        RxTest rxTest = new RxTest();
        rxTest.init();
        rxTest.start();
    }
}
