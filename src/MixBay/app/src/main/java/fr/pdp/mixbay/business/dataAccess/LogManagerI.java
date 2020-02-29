package fr.pdp.mixbay.business.dataAccess;

import fr.pdp.mixbay.business.models.LogItem;

public interface LogManagerI {

    /**
     * Create the log file.
     */
    void create();

    /**
     * Append the given item to the Log list
     * @param item Item to add.
     */
    void append(LogItem item);

    /**
     * Save the list into the log file.
     */
    void save();
}
