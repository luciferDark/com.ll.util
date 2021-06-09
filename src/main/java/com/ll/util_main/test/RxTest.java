package com.ll.util_main.test;

import com.ll.internet.interfaces.TestRxRetrofit;
import com.ll.utils.Log;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RxTest {

    public void init() {

    }

    public void start() {
        Log.log("start");

//        testOnCreate();
        testRxRetrofit();
    }

    private void testRxRetrofit() {
        Log.log("testRxRetrofit");
        String url = "https://ons.ooo/type/1/";
        String url1 = "https://www.baidu.com/";
        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        TestRxRetrofit service = retrofit.create(TestRxRetrofit.class);
        Observable<Response<String>> observable = service.getData();
        observable.subscribe(new Consumer<Response>() {
            @Override
            public void accept(Response response) throws Throwable {
                if (response != null && response.isSuccessful()) {
                    parse(response.body().toString());
                }
            }
        });

    }

    private void parse(String html) {
        Document doc = Jsoup.parseBodyFragment(html);
        Log.log("parse:", doc.body().toString());
    }

    private void testOnCreate() {
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onNext("3");
                emitter.onNext("111");
                emitter.onComplete();
                Log.log("emitter onComplete");
            }
        });
        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.log("observer onSubscribe");
            }

            @Override
            public void onNext(String msg) {
                Log.log("observer onNext", msg);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.log("observer onError:", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.log("observer onComplete");
            }
        };

        observable.subscribe(observer);
        observable.doOnDispose(new Action() {
            @Override
            public void run() throws Throwable {

            }
        });

        Consumer consumer1 = new Consumer() {
            @Override
            public void accept(Object o) throws Throwable {

                Log.log("consumer1 ");
            }
        };
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String msg) {
                Log.log("consumer accept", msg);
            }
        };
        Consumer<Disposable> consumerDisposable = new Consumer<Disposable>() {

            @Override
            public void accept(Disposable disposable) throws Throwable {
                Log.log("consumer accept Disposable", disposable.toString());
            }
        };
        observable.subscribe(consumer, consumerDisposable);
    }
}
