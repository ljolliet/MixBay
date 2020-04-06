/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.data;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.pdp.mixbay.business.dataAccess.LogManagerI;
import fr.pdp.mixbay.business.models.LogItem;

public class JSONLogManager implements LogManagerI {
    private static final String USERNAME = "username";
    private static final String ACTION = "action";
    private static final String DETAILS = "details";
    private static final String TRACKNAME = "trackname";
    private static final String ALGO = "algorithm";
    private static final String DATE = "time";
    private static final String LOG = "log";
    private List<LogItem> items = new ArrayList<>();

    private JSONObject root = new JSONObject();
    private JSONArray array = new JSONArray();
    private String filename = "/mixbay_LOG_";
    private String extension = ".json";
    private String folderName = "/MixBay/";
    private File outputFile;

    @Override
    public void create() {
        //Create folder if does not exist
        File folder = new File(Environment.getExternalStorageDirectory()
                + folderName);
        if (!folder.exists()) {
            folder.mkdir();
            Log.d("Folder", "Folder Created");
        }
        //Build the log file
        String pattern = "dd-MM-yyyy";
        Locale locale = new Locale("fr", "FR");
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, locale);
        filename = filename + simpleDateFormat.format(new Date()) + extension;
        outputFile = new File(Environment.getExternalStorageDirectory()
                + folderName + filename);
        Log.d("JsonLogManager", Environment.getExternalStorageDirectory()
                + filename);
    }

    @Override
    public void append(LogItem item) {
        this.items.add(item);
        this.save();
    }

    @Override
    public void save() {

        try {
            for (LogItem i : this.items) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put(USERNAME, i.username);
                jsonItem.put(ACTION, i.action);
                jsonItem.put(DETAILS, i.details);
                jsonItem.put(TRACKNAME, i.trackName);
                jsonItem.put(ALGO, i.algorithm);
                jsonItem.put(DATE, i.date);
                array.put(jsonItem);
            }
            root.put(LOG, array);
            Log.d("JSONLogManager", root.toString());
            FileOutputStream file = new FileOutputStream(outputFile);

            // get the content in bytes
            byte[] contentInBytes = root.toString().getBytes();

            file.write(contentInBytes);
            file.flush();
            file.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
