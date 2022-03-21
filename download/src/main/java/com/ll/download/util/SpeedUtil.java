package com.ll.download.util;

import java.text.DecimalFormat;

public class SpeedUtil {
    public static String FormatSize(long length) {
        DecimalFormat df = new DecimalFormat("0.00");
        String sizeResult = "";
        if (0 <= length && length < 1024) {
            sizeResult = df.format(length) + "b/s";
        } else if (1024 <= length && length < 1048576) {
            sizeResult = df.format(((double)length)/1024) + "kb/s";
        } else if (1048576 <= length && length < 1073741824) {
            sizeResult = df.format(((double)length)/1048576) + "m/s";
        } else {
            sizeResult = df.format(((double)length)/1073741824) + "g/s";
        }
        return sizeResult;
    }
}
