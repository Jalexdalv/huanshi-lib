package org.huanshi.mc.lib.utils;

public class FormatUtils {
    public static long convertDurationToTick(final long duration) {
        return (long) Math.ceil((double) duration / (double) 50);
    }

    public static int convertMillisecondToSecond(final long millisecond) {
        return (int) Math.ceil((double) millisecond / (double) 1000);
    }
}
