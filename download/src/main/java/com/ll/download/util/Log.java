package com.ll.download.util;

public class Log {
    public static void log(Object... msgs){
        StringBuilder builder = new StringBuilder();
        for (Object msg : msgs) {
            builder.append(msg).append("\t");
        }
        System.out.println(builder.toString());
    }
}
