package fr.pdp.mixbay.data;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    private JSONArray array= new JSONArray();
    private String filename = "/mixbay_LOG_";
    private String extension = ".json";
    private File outputFile;

    @Override
    public void create() {
        String pattern = "dd-MM-yyyy";
        Locale locale = new Locale("fr", "FR");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        filename = filename + simpleDateFormat.format(new Date()) + extension;
        outputFile = new File(Environment.getExternalStorageDirectory() + filename);
        Log.d("JsonLogManager", Environment.getExternalStorageDirectory() + filename);

        //TODO manage all possible cases
    }

    @Override
    public void append(LogItem item) {
        this.items.add(item);
        try {
            this.save();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save() throws JSONException, IOException {
        for(LogItem i : this.items)
        {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put(USERNAME, i.username);
            jsonItem.put(ACTION,i.action);
            jsonItem.put(DETAILS,i.details);
            jsonItem.put(TRACKNAME,i.trackName);
            jsonItem.put(ALGO,i.algorithm);
            jsonItem.put(DATE,i.date);
            array.put(jsonItem);
        }
        root.put(LOG, array);
        Log.d("JSONLogManager", root.toString());
        FileOutputStream file= new FileOutputStream(outputFile);

        // get the content in bytes
        byte[] contentInBytes = root.toString().getBytes();

        file.write(contentInBytes);
        file.flush();
        file.close();

        return true;
    }
}
