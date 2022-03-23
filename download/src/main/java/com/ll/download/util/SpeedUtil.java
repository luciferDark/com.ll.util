package com.ll.download.util;

import java.text.DecimalFormat;

public class SpeedUtil {
    public static String FormatSize(double length) {
        DecimalFormat df = new DecimalFormat("0.00");
        String sizeResult = "";
//        Log.log("length", length);
        if (0 <= length && length < 1024) {
            sizeResult = df.format(length) + "b/s";
        } else if (1024 <= length && length < 1048576) {
            sizeResult = df.format(((double) length) / 1024) + "Kb/s";
        } else if (1048576 <= length && length < 1073741824) {
            sizeResult = df.format(((double) length) / 1048576) + "Mb/s";
        } else {
            sizeResult = df.format(((double) length) / 1073741824) + "Gb/s";
        }
        return sizeResult;
    }
}
