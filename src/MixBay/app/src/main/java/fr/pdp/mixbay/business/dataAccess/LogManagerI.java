package fr.pdp.mixbay.business.dataAccess;

import org.json.JSONException;

import java.io.IOException;

import fr.pdp.mixbay.business.models.LogItem;

public interface LogManagerI {

    void create();
    void append(LogItem item);
    boolean save() throws JSONException, IOException;
}
