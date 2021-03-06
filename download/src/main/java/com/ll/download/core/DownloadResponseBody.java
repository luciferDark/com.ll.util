package com.ll.download.core;

import com.ll.download.beans.DownloadBean;
import com.ll.download.listener.DownloadProcessListener;
import com.ll.download.util.Log;
import com.ll.download.util.SpeedUtil;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import java.io.IOException;

public class DownloadResponseBody extends ResponseBody {
    private DownloadBean item;
    private ResponseBody responseBody;
    private DownloadProcessListener listener;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, DownloadProcessListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    public DownloadResponseBody(DownloadBean item, ResponseBody responseBody, DownloadProcessListener listener) {
        this.item = item;
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (null == bufferedSource){
            bufferedSource = Okio.buffer(rebuildSource(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source rebuildSource(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = item.getContentLength();
            long lastTimeStamp = 0;
            double speed = 0;
            double lastTimeSpeed = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead == -1 ? 0 : bytesRead;
                 if (lastTimeStamp == 0 || bytesRead == -1){
                     speed = 0;
                 } else {
                     long timeStep = Math.abs(System.currentTimeMillis() - lastTimeStamp);
                     if ( timeStep == 0) {
                         speed = lastTimeSpeed;
                     } else{
//                         Log.log("bytesRead",bytesRead,"timeStep",timeStep);
                         speed = ((double)bytesRead * 1000)/ timeStep;
                     }
                 }
                lastTimeSpeed = speed;
                lastTimeStamp = System.currentTimeMillis();
                if (null != listener){
                    listener.onProcess(item, totalBytesRead, contentLength(),
                            bytesRead == -1, speed);
                }
                return bytesRead;
            }
        };
    }
}
