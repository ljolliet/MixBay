package fr.pdp.mixbay.models;

import android.content.Context;

import java.util.Set;

import fr.pdp.mixbay.MainActivity;

public class Session {

    private Playlist localePlaylist;
    private User mainUser;
    private User currentuser;
    private Set<User> users;
    private AlgoI algo;
    private LogManagerI logManager;
    private APIManagerI apiManager;
    
    public Session(){
        
    }

    public Session(APIManagerI api) {
        this.apiManager = api;
    }

    public void start(Context mainActivity) {
        apiManager.connect(mainActivity);
    }

    public void end() {
        apiManager.disconnect();
    }
}
