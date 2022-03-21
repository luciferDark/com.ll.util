package com.ll.util_main.test;

import com.ll.internet.interfaces.TestRxRetrofit;
import com.ll.utils.Log;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.*;

public class RxTest {
    private String urlbase = "https://ons.ooo";
    private Map<String, String> resultChapter = null;
    private Map<String, Set<String>> resultList = null;
    int maxNumFor = 3;
    int countNow = 0;
    int count = 0;

    public void init() {
        resultChapter = new HashMap<>();
        resultList = new HashMap<>();
    }

    public void start() {
        String path = "";
        String search = "朱可儿";
        String page = null;

        parse(path, search, page, true, false, null, -1);
    }

    private void parse(String path, String search, String page,
                       boolean isSearch, boolean isGetAllPic, String title, int process) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(urlbase)
                .build();

        TestRxRetrofit service = retrofit.create(TestRxRetrofit.class);
        Observable<Response<String>> observable;
        if (isSearch) {
            observable = service.searchData(search, page);
        } else {
            observable = service.getData(path, page);
        }

        Log.log("number:", String.valueOf(countNow),
                "url:", urlbase, "path:", path, "search:", search, "page:", page, "process:", process);
        observable.subscribe((Consumer<Response>) response -> {
            if (response != null && response.isSuccessful()) {
                Document doc = Jsoup.parseBodyFragment(response.body().toString());
                if (isGetAllPic) {
                    parseAllPicsUrl(path, title, doc, process);
                } else {
                    parseAllPages(path, search, isSearch, doc);
                }
            } else {

                Log.log("errorBody:", response.raw().toString());
            }
        }, throwable -> {
            Log.log("Error", throwable.getLocalizedMessage());
            System.exit(0);
        });
    }

    private void parseAllPages(String path,
                               String search,
                               boolean isSearch,
                               Document doc) {
        countNow++;
        List<Element> h2Element = doc.body().getElementsByTag("h2");
        for (Element element : h2Element) {
            Element elementA = element.getElementsByTag("a").get(0);
            String title = elementA.ownText();
            String itemUrl = elementA.attr("href");
            if (!resultChapter.containsKey(title)) {
                resultChapter.put(title, itemUrl);
            }
        }
        List<Element> pages = doc.body().getElementsByClass("next");
        Element next = pages == null ? null : (pages.size() > 0 ? pages.get(0) : null);
        if (next == null || countNow >= maxNumFor) {
            doNextStep();
            return;
        }

        String page = next.attr("href");

        if (isSearch && page.contains("&")) {
            page = page.split("&")[0].replace("?page=", "");
        } else {
            page = page.replace("?page=", "");
        }
        Log.log("page:", page);
        boolean isGetAllPic = false;
        parse(path, search, page, isSearch, isGetAllPic, null, -1);
    }

    private void doNextStep() {
        final int size = resultChapter.size();
        Log.log("resultChapter size is", size);
        resultChapter.forEach((title, url) -> {
//            Log.log("title:",title, "\turl:",url);
            count++;
            boolean isLast = (count > size - 1 ? true : false);
            Log.log("isLast:", isLast, "count:", count, "size:", size);
            parse(url.replace("/a", "a"), null, null, false, true,
                    title, count);
        });
    }

    private void parseAllPicsUrl(String path, String title, Document doc, int process) {
        List<Element> h2Element = doc.body().getElementsByTag("p");
        Set<String> allPics = new HashSet<>();
        for (Element element : h2Element) {
            Element elementA = element.getElementsByTag("img").get(0);
            String itemUrl = elementA.attr("data-original");
            allPics.add(itemUrl);
        }
        if (!resultList.containsKey(title)) {
            resultList.put(title, allPics);
        }
        Log.log("process", process);
        if (process >= resultChapter.size()) {
            //最后一个解析完成
            Log.log("resultList size is", resultList.size());
            resultList.forEach((titleKey, set) -> {
                Log.log("title:", titleKey, "\turls:", printSet(set));
            });
        }
    }

    private String printSet(Set<?> set) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (Object str : set) {
            builder.append(str);//.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

}
