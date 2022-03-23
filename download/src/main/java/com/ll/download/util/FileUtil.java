package com.ll.download.util;

import com.ll.download.beans.DownloadBean;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

public class FileUtil {
    public static void writeToDisk(ResponseBody response, DownloadBean bean) throws IOException {
        if (TextUtil.empty(bean.getDiskPath()) || TextUtil.empty(bean.getFileName()))
            return;
        String fullPath = bean.getDiskFullPath();
        Log.log("write to disk ", bean.getUrl(), "into", fullPath);
        File fileDisk = new File(fullPath);
        if (!fileDisk.getParentFile().exists()) {
            fileDisk.getParentFile().mkdirs();
        }
        long allLength = 0;
        if (bean.getContentLength() == 0
                || bean.getContentLength() != response.contentLength()) {
            allLength = response.contentLength();
            bean.setContentLength(allLength);
        } else {
            allLength = bean.getContentLength();
        }

        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;

        randomAccessFile = new RandomAccessFile(fileDisk, "rwd");
        fileChannel = randomAccessFile.getChannel();

        MappedByteBuffer byteBuffer = fileChannel.map(
                FileChannel.MapMode.READ_WRITE,
                bean.getCurrentLength(),
                allLength - bean.getCurrentLength()
        );


        byte[] buffer = new byte[1024 * 10];
        int len = 0;
        while ((len = response.byteStream().read(buffer)) != -1) {
            byteBuffer.put(buffer, 0, len);
        }
        byteBuffer.force();

        response.byteStream().close();
        if (fileChannel != null) fileChannel.close();
        if (randomAccessFile != null) randomAccessFile.close();
    }

    public static void deletFromDisk(String path) {
        if (TextUtil.empty(path))
            return;
        File file = new File(path);
        if (file.exists()) file.delete();
    }
}
