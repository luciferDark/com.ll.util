package com.ll.internet.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestRetrofit {

    @GET("/")
    Call<String> getData();
}
