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

import android.os.Environment;

import java.io.*;

/**
 * A utility class for basic file operations
 *
 * @author mehmetakiftutuncu
 */
public class FileUtils {
    /** Full path of data folder to store KentKart data in external storage of device */
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DATA_PATH;

    /** A File object pointing to {@link com.mehmetakiftutuncu.mykentkart.utilities.FileUtils#DATA_PATH} */
    public static File dataPath = getDataPath();

    /**
     * Reads a file with given name from data folder into a String
     *
     * @param fileName Name of the file in data folder to read
     *
     * @return A String with contents of the file with given name or null if any error occurs
     */
    public static String readFile(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            Log.error(FileUtils.class, "Failed to read file, file name is empty!");
            return null;
        } else {
            if (dataPath == null) {
                Log.error(FileUtils.class, "Failed to read file, data path is null! fileName: " + fileName);
                return null;
            } else {
                File file = new File(dataPath.getAbsolutePath() + "/" + fileName);

                if (!file.exists() || !file.canRead()) {
                    Log.error(FileUtils.class, "Failed to read file, file cannot be accessed! file: " + file);
                    return null;
                } else {
                    try {
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        StringBuilder stringBuilder = new StringBuilder();

                        while (bufferedReader.ready()) {
                            stringBuilder.append(bufferedReader.readLine()).append("\n");
                        }
                        bufferedReader.close();

                        return stringBuilder.toString();
                    } catch (Exception e) {
                        Log.error(FileUtils.class, "Failed to read file! file: " + file, e);
                        return null;
                    }
                }
            }
        }
    }

    /**
     * Writes given String to a file with given name in data folder
     *
     * @param data     Data to write
     * @param fileName Name of the file to which data will be written
     *
     * @return true if successfully written or false if any error occurs
     */
    public static boolean writeFile(String data, String fileName) {
        if (StringUtils.isEmpty(data)) {
            Log.error(FileUtils.class, "Failed to write file, data is empty! fileName: " + fileName);
            return false;
        } else if (StringUtils.isEmpty(fileName)) {
            Log.error(FileUtils.class, "Failed to write file, file name is empty! data: " + data);
            return false;
        } else {
            if (dataPath == null) {
                Log.error(FileUtils.class, "Failed to write file, data path is null! data: " + data + ", fileName: " + fileName);
                return false;
            } else {
                File file = new File(dataPath.getAbsolutePath() + "/" + fileName);
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    bufferedWriter.write(data);

                    bufferedWriter.flush();
                    bufferedWriter.close();

                    return true;
                } catch (Exception e) {
                    Log.error(FileUtils.class, "Failed to write file! data: " + data + ", file: " + file, e);
                    return false;
                }
            }
        }
    }

    /**
     * Gets a File object pointing to {@link com.mehmetakiftutuncu.mykentkart.utilities.FileUtils#DATA_PATH}
     * making sure that all folders in the path exist
     *
     * @return A File object pointing to data folder or null if any error occurs
     */
    public static File getDataPath() {
        File path = new File(DATA_PATH);

        if ((!path.exists() && !path.mkdirs()) || !path.canRead()) {
            Log.error(Data.class, "Failed to get data path, data directory cannot be accessed!");
            return null;
        } else {
            return path;
        }
    }
}
