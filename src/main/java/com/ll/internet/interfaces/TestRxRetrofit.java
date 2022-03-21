package com.ll.internet.interfaces;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TestRxRetrofit {

    @GET("{path}")
    Observable<Response<String>> getData(@Path("path") String path,
                                         @Query("page") String page);

    @GET("/search/")
    Observable<Response<String>> searchData(@Query("s") String search,
                                            @Query("page") String page);
}
