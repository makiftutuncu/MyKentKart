/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.mykentkart.utilities;

import com.mehmetakiftutuncu.mykentkart.BuildConfig;

/**
 * A utility class for easily logging stuff, basically a wrapper over {@link android.util.Log}
 *
 * @author mehmetakiftutuncu
 */
public class Log {
    /**
     * Generates a tag for the logging using given reference to the object calling this logger
     *
     * @param loggingObject A reference to the object calling this logger
     *
     * @return A tag for the logging containing information about the caller of this logger
     */
    private static String getTag(Object loggingObject) {
        return String.format("MyKentKart.%s", loggingObject.getClass().getSimpleName());
    }

    /**
     * Logs given message with {@link android.util.Log#INFO} logging level
     *
     * @param loggingObject A reference to the object calling this logger
     * @param message       Message to log
     */
    public static void info(Object loggingObject, String message) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(getTag(loggingObject), message);
        }
    }

    /**
     * Logs an error with given message with {@link android.util.Log#ERROR} logging level
     *
     * @param loggingObject A reference to the object calling this logger
     * @param message       Error message to log
     */
    public static void error(Object loggingObject, String message) {
        android.util.Log.e(getTag(loggingObject), message);
    }

    /**
     * Logs an error with given message with {@link android.util.Log#ERROR} logging level,
     * including the stack trace of the error
     *
     * @param loggingObject A reference to the object calling this logger
     * @param message       Error message to log
     * @param error         Throwable object of the error for getting stack trace
     */
    public static void error(Object loggingObject, String message, Throwable error) {
        android.util.Log.e(getTag(loggingObject), message, error);
    }
}
