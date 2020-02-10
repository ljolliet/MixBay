package fr.pdp.mixbay.models;

import java.util.Set;

public class FirstAlgo implements AlgoI {


    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute(Set<User> users) {
        return null;
    }

}
