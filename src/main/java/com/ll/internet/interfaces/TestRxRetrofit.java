package com.ll.internet.interfaces;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface TestRxRetrofit {

    @GET("/")
    Observable<Response<String>> getData();
}
