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

import com.mehmetakiftutuncu.mykentkart.models.KentKart;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * A utility class that provides basic functionality over KentKart data
 *
 * @author mehmetakiftutuncu
 */
public class Data {
    /**
     * Loads a saved KentKart with given key from data folder
     *
     * @param key Key of the KentKart to load, either it's number or it's NFC id
     *
     * @return Loaded KentKart object or null if any error occurs
     */
    public static KentKart loadKentKart(final String key) {
        if (StringUtils.isEmpty(key)) {
            Log.error(Data.class, "Failed to load KentKart, key is empty!");
            return null;
        } else {
            if (FileUtils.dataPath == null) {
                Log.error(Data.class, "Failed to load KentKart, data path is null! key: " + key);
                return null;
            } else {
                String[] matchingFileNames = FileUtils.dataPath.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(key);
                    }
                });

                if (matchingFileNames.length == 0) {
                    Log.error(Data.class, "Failed to load KentKart, file not found! key: " + key);
                    return null;
                } else if (matchingFileNames.length > 1) {
                    Log.error(Data.class, "Failed to load KentKart, more than 1 files found! key: " + key);
                    return null;
                } else {
                    String fileName = matchingFileNames[0];
                    String data = FileUtils.readFile(fileName);

                    if (StringUtils.isEmpty(data)) {
                        Log.error(Data.class, "Failed to load KentKart, loaded data is empty! key: " + key);
                        return null;
                    } else {
                        KentKart kentKart = KentKart.fromJson(data);

                        if (kentKart == null) {
                            Log.error(Data.class, "Failed to load KentKart, KentKart object is empty! key: " + key);
                            return null;
                        } else {
                            return kentKart;
                        }
                    }
                }
            }
        }
    }

    /**
     * Saves given KentKart to data folder
     *
     * @param kentKart KentKart object to save
     *
     * @return true if successfully saved or false if any error occurs
     */
    public static boolean saveKentKart(final KentKart kentKart) {
        if (kentKart == null) {
            Log.error(Data.class, "Failed to save KentKart, KentKart is null!");
            return false;
        } else {
            if (FileUtils.dataPath == null) {
                Log.error(Data.class, "Failed to save KentKart, data path is null! kentKart: " + kentKart);
                return false;
            } else {
                String data = kentKart.toJson();

                return FileUtils.writeFile(data, getKentKartFileName(kentKart));
            }
        }
    }

    /**
     * Deletes given saved KentKart from data folder
     *
     * @param kentKart KentKart object to delete
     *
     * @return true if successfully deleted or false if any error occurs
     */
    public static boolean deleteKentKart(final KentKart kentKart) {
        if (kentKart == null) {
            Log.error(Data.class, "Failed to delete KentKart, KentKart is null!");
            return false;
        } else {
            if (FileUtils.dataPath == null) {
                Log.error(Data.class, "Failed to delete KentKart, data path is null! kentKart: " + kentKart);
                return false;
            } else {
                File[] matchingFiles = FileUtils.dataPath.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        boolean containsNumber = pathname.getName().contains(kentKart.number);
                        boolean containsNfcId = !StringUtils.isEmpty(kentKart.nfcId) && pathname.getName().contains(kentKart.nfcId);

                        return containsNumber || containsNfcId;
                    }
                });

                boolean result = true;

                for (File file : matchingFiles) {
                    result &= file.delete();
                }

                return result;
            }
        }
    }

    /**
     * Generates name of the file to contain data of given KentKart
     *
     * @param kentKart KentKart object from which to generate file name
     *
     * @return Name of the file to contain data of given KentKart or null when KentKart is invalid
     */
    private static String getKentKartFileName(KentKart kentKart) {
        if (kentKart == null || StringUtils.isEmpty(kentKart.number)) {
            return null;
        } else {
            return String.format("%s.kentkart", StringUtils.isEmpty(kentKart.nfcId) ? kentKart.number : kentKart.nfcId);
        }
    }
}
