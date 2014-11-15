package com.mehmetakiftutuncu.mykentkart.tasks;

import android.os.AsyncTask;

import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.FileUtils;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class LoadKentKartsTask extends AsyncTask<Void, Void, ArrayList<KentKart>> {
    public interface OnKentKartsLoadedListener {
        public void onKentKartsLoaded(ArrayList<KentKart> kentKarts);
    }

    private OnKentKartsLoadedListener listener;

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
                    ArrayList<KentKart> kentKarts = new ArrayList<KentKart>();

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
