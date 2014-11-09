package com.mehmetakiftutuncu.mykentkart.utilities;

import com.mehmetakiftutuncu.mykentkart.models.KentKart;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public final class Data {
    public static KentKart loadKentKart(final String key) {
        if (StringUtils.isEmpty(key)) {
            Log.error(Data.class, "Failed to load Kent Kart, key is empty!");
            return null;
        } else {
            File dataPath = FileUtils.getDataPath();

            if (dataPath == null) {
                Log.error(Data.class, "Failed to load Kent Kart, data path is null! key: " + key);
                return null;
            } else {
                String[] matchingFileNames = dataPath.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(key);
                    }
                });

                if (matchingFileNames.length == 0) {
                    Log.error(Data.class, "Failed to load Kent Kart, file not found! key: " + key);
                    return null;
                } else if (matchingFileNames.length > 1) {
                    Log.error(Data.class, "Failed to load Kent Kart, more than 1 files found! key: " + key);
                    return null;
                } else {
                    String fileName = matchingFileNames[0];
                    String data = FileUtils.loadFile(fileName);

                    if (StringUtils.isEmpty(data)) {
                        Log.error(Data.class, "Failed to load Kent Kart, loaded data is empty! key: " + key);
                        return null;
                    } else {
                        KentKart kentKart = KentKart.fromJson(data);

                        if (kentKart == null) {
                            Log.error(Data.class, "Failed to load Kent Kart, Kent Kart object is empty! key: " + key);
                            return null;
                        } else {
                            return kentKart;
                        }
                    }
                }
            }
        }
    }

    public static boolean saveKentKart(final KentKart kentKart) {
        if (kentKart == null) {
            Log.error(Data.class, "Failed to save Kent Kart, Kent Kart is null!");
            return false;
        } else {
            File dataPath = FileUtils.getDataPath();

            if (dataPath == null) {
                Log.error(Data.class, "Failed to save Kent Kart, data path is null! kentKart: " + kentKart);
                return false;
            } else {
                File[] matchingFiles = dataPath.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        boolean containsNumber = pathname.getName().contains(kentKart.number);
                        boolean containsNfcId = pathname.getName().contains(kentKart.nfcId);

                        return containsNumber || containsNfcId;
                    }
                });

                for (File file : matchingFiles) {
                    file.delete();
                }

                String data = kentKart.toJson();

                return FileUtils.saveFile(data, getKentKartFileName(kentKart));
            }
        }
    }

    private static String getKentKartFileName(KentKart kentKart) {
        if (kentKart == null) {
            return null;
        } else if (StringUtils.isEmpty(kentKart.number)) {
            return null;
        } else if (StringUtils.isEmpty(kentKart.nfcId)) {
            return String.format("%s.kentkart", kentKart.number);
        } else {
            return String.format("%s.%s.kentkart", kentKart.number, kentKart.nfcId);
        }
    }
}
