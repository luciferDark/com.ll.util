package com.ll.util_main;

import com.ll.util_main.test.RxTest;

public class Main {

    public static void main(String[] args) {
        log("main start");
        test();
    }

    private static void test() {
        RxTest rxTest = new RxTest();
        rxTest.init();
        rxTest.start();

    }


    public static void log(String logs) {
        System.out.println(logs);
    }
}
