package com.ll.util_main;

import com.ll.util_main.test.RxTest;
import com.ll.utils.Log;

public class Main {

    public static void main(String[] args) {
        Log.log("main start");
        test();
    }

    private static void test() {
        RxTest rxTest = new RxTest();
        rxTest.init();
        rxTest.start();


        DownloadBean
    }
}
