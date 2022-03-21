package com.ll.internet.downloadUrlImgs.onso;

import com.ll.internet.models.onso.ChapterBody;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface OnsoApi {

    @GET("/")
    public Observable<ChapterBody> getChapterList();
}
