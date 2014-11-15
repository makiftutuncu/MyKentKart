package com.mehmetakiftutuncu.mykentkart.utilities;

import com.mehmetakiftutuncu.mykentkart.models.KentKart;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public final class Data {
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
