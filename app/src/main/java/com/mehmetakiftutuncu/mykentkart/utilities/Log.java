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

public class Log {
    private static String getTag(Object loggingObject) {
        return String.format("MyKentKart.%s", loggingObject.getClass().getSimpleName());
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
