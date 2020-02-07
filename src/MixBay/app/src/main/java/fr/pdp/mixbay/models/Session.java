package fr.pdp.mixbay.models;

import java.util.Set;

public class Session {

    private Playlist localePlaylist;
    private User mainUser;
    private User currentuser;
    private Set<User> users;
    private AlgoI algo;
    private LogManagerI logManager;
    private APIManagerI apiManager;

}
