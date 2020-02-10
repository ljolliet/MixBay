package fr.pdp.mixbay.models;

import java.util.Set;

public interface AlgoI {

    public String getName();
    public Playlist compute(Set<User> users);
}
