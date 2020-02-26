package fr.pdp.mixbay.business.dataAccess;

import fr.pdp.mixbay.business.models.LogItem;

public interface LogManagerI {

    void create();
    void append(LogItem item);
    boolean save();
}
