package com.ll.download.apis;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadRetrofitApi {
    @GET
    @Streaming
    Observable<ResponseBody> download(@Header("RANGE") String startIndex, @Url String url);
}
