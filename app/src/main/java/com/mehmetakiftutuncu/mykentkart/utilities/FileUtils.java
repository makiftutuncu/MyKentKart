package com.mehmetakiftutuncu.mykentkart.utilities;

import android.os.Environment;

import java.io.*;

public class FileUtils {
    private static final String DATA_PATH = Environment.getDataDirectory().getAbsolutePath() + File.separator + "com.mehmetakiftutuncu.mykentkart";

    public static String loadFile(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            Log.error(FileUtils.class, "Failed to load file, file name is empty!");
            return null;
        } else {
            File dataPath = getDataPath();

            if (dataPath == null) {
                Log.error(FileUtils.class, "Failed to load file, data path is null! fileName: " + fileName);
                return null;
            } else {
                File file = new File(dataPath.getAbsolutePath() + File.separator + fileName);

                if (!file.exists() || !file.canRead()) {
                    Log.error(FileUtils.class, "Failed to load file, file cannot be accessed! file: " + file);
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
                        Log.error(FileUtils.class, "Failed to load file with exception! file: " + file, e);
                        return null;
                    }
                }
            }
        }
    }

    public static boolean saveFile(String data, String fileName) {
        if (StringUtils.isEmpty(data)) {
            Log.error(FileUtils.class, "Failed to save file, data is empty! fileName: " + fileName);
            return false;
        } else if (StringUtils.isEmpty(fileName)) {
            Log.error(FileUtils.class, "Failed to save file, file name is empty! data: " + data);
            return false;
        } else {
            File dataPath = getDataPath();

            if (dataPath == null) {
                Log.error(FileUtils.class, "Failed to save file, data path is null! data: " + data + ", fileName: " + fileName);
                return false;
            } else {
                File file = new File(dataPath.getAbsolutePath() + File.separator + fileName);
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    bufferedWriter.write(data);

                    bufferedWriter.flush();
                    bufferedWriter.close();

                    return true;
                } catch (Exception e) {
                    Log.error(FileUtils.class, "Failed to save file with exception! data: " + data + ", file: " + file, e);
                    return false;
                }
            }
        }
    }

    public static File getDataPath() {
        File dataPath = new File(DATA_PATH);

        if ((!dataPath.exists() && !dataPath.mkdirs()) || !dataPath.canRead()) {
            Log.error(Data.class, "Failed to get data path, data directory cannot be accessed!");
            return null;
        } else {
            return dataPath;
        }
    }
}
