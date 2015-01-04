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
package com.mehmetakiftutuncu.mykentkart.tasks;

import android.os.AsyncTask;

import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.FileUtils;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * An {@link android.os.AsyncTask} to load the list of saved {@link com.mehmetakiftutuncu.mykentkart.models.KentKart}s
 *
 * @author mehmetakiftutuncu
 */
public class LoadKentKartsTask extends AsyncTask<Void, Void, ArrayList<KentKart>> {
    /**
     * An interface to indicate when loading the list of
     * {@link com.mehmetakiftutuncu.mykentkart.models.KentKart}s is finished and data is ready
     */
    public interface OnKentKartsLoadedListener {
        /**
         * Indicates that loading the list of
         * {@link com.mehmetakiftutuncu.mykentkart.models.KentKart}s is finished and data is ready
         *
         * @param kentKarts An {@link java.util.ArrayList} of {@link com.mehmetakiftutuncu.mykentkart.models.KentKart}s loaded
         */
        public void onKentKartsLoaded(ArrayList<KentKart> kentKarts);
    }

    /**
     * A reference to the object implementing
     * {@link com.mehmetakiftutuncu.mykentkart.tasks.LoadKentKartsTask.OnKentKartsLoadedListener}
     * to notify when task is finished
     */
    private OnKentKartsLoadedListener listener;

    /**
     * Constructor initializing all values
     *
     * @param listener Value to set as {@link com.mehmetakiftutuncu.mykentkart.tasks.LoadKentKartsTask#listener}
     */
    public LoadKentKartsTask(OnKentKartsLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<KentKart> doInBackground(Void... params) {
        if (listener == null) {
            Log.error(this, "Failed to load all KentKarts, listener is null!");
            return null;
        } else {
            try {
                File dataPath = FileUtils.getDataPath();

                if (dataPath == null) {
                    Log.error(this, "Failed to load all KentKarts, data path is null!");
                    return null;
                } else {
                    ArrayList<KentKart> kentKarts = new ArrayList<>();

                    String[] fileNames = dataPath.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.endsWith(".kentkart");
                        }
                    });

                    for (String fileName : fileNames) {
                        String data = FileUtils.readFile(fileName);

                        if (StringUtils.isEmpty(data)) {
                            Log.error(this, "Failed to load KentKart, loaded data is empty! fileName: " + fileName);
                        } else {
                            KentKart kentKart = KentKart.fromJson(data);

                            if (kentKart != null) {
                                kentKarts.add(kentKart);
                            }
                        }
                    }

                    return kentKarts;
                }
            } catch (Exception e) {
                Log.error(this, "Failed to load all KentKarts!", e);
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList<KentKart> kentKarts) {
        if (listener != null) {
            listener.onKentKartsLoaded(kentKarts);
        }
    }
}
