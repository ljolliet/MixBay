package fr.pdp.mixbay.data;

import java.util.ArrayList;
import java.util.List;

import fr.pdp.mixbay.business.dataAccess.LogManagerI;
import fr.pdp.mixbay.business.models.LogItem;

public class JSONLogManager implements LogManagerI {
    private List<LogItem> items = new ArrayList<>();

    @Override
    public void create() {

    }

    @Override
    public void append(LogItem item) {
        this.items.add(item);
    }

    @Override
    public boolean save() {
        for(LogItem i : this.items)
        {
            //TODO Manage JSON
        }
        return false;
    }
}
