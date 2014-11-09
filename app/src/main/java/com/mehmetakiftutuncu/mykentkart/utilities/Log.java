package com.mehmetakiftutuncu.mykentkart.utilities;

import com.mehmetakiftutuncu.mykentkart.BuildConfig;

public class Log {
    private static String getTag(Object loggingObject) {
        return String.format("Cardroid.%s", loggingObject.getClass().getSimpleName());
    }

    public static void info(Object loggingObject, String message) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(getTag(loggingObject), message);
        }
    }

    public static void error(Object loggingObject, String message) {
        android.util.Log.e(getTag(loggingObject), message);
    }

    public static void error(Object loggingObject, String message, Throwable error) {
        android.util.Log.e(getTag(loggingObject), message, error);
    }
}
