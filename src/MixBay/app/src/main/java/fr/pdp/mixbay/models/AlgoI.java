package fr.pdp.mixbay.models;

import java.util.Set;

public interface AlgoI {

    public void setUsers(Set<User> users);

    public String getName();
    public Playlist compute();
}
