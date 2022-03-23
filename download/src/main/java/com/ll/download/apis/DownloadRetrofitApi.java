package com.ll.download.apis;

import com.ll.download.core.DownloadResponseBody;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadRetrofitApi {
    @GET
    @Streaming
    Observable<ResponseBody> downloadByRange(@Header("RANGE") String startIndex, @Url String url);
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String url);
}
