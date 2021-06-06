package com.ll.utils;

public class Log {
    public static void log(String... msgs){
        StringBuilder builder = new StringBuilder();
        for (String msg : msgs) {
            builder.append(msg).append("\t");
        }
        System.out.println(builder.toString());
    }
}
