package com.ll.download.configs;

public class DownloadError {
    public final static int Error_DownLoadInfoError = 0x200001;
    public final static int Error_DownLoadDuplicate = 0x200002;
    public final static int Error_DownLoadError = 0x200003;


    public static String getMsg(int code) {
        String msg = "";
        switch (code) {
            case Error_DownLoadInfoError:
                msg = "下载任务异常";
                break;
            case Error_DownLoadDuplicate:
                msg = "下载任务重复,正在下载中";
                break;
            case Error_DownLoadError:
                msg = "下载任务过程出错:";
                break;
        }

        return msg;
    }
}
