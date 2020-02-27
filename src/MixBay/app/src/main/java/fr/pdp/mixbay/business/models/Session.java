package fr.pdp.mixbay.business.models;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.algorithms.LeastMisery;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.LogManagerI;
import fr.pdp.mixbay.data.JSONLogManager;


public class Session {

    private LocalPlaylist localPlaylist;
    private User currentuser;
    private Set<User> users;
    private AlgoI algo;
    private LogManagerI logManager;
    private APIManagerI apiManager;
    private boolean mixed;

    public Session(APIManagerI api) {
        this.apiManager = api;
        this.localPlaylist = new LocalPlaylist();
        this.users = new HashSet<>();
        this.algo = new LeastMisery();
        this.logManager = new JSONLogManager();
        this.mixed = false;
    }

    public void start(Context context) {
        apiManager.connect(context);
        Services.randomInit(this);
    }

    public void generatePlaylist(){
        localPlaylist = algo.compute(this.users);
        this.mixed = true;
    }


    public boolean end() {
        return apiManager.disconnect();
    }


    public void setCurrentUser(User currentuser) {
        this.currentuser = currentuser;
    }

    public void setAlgo(AlgoI algo) {
        this.algo = algo;
    }

    public void setLogManager(LogManagerI logManager) {
        this.logManager = logManager;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public APIManagerI getApi() {
        return apiManager;
    }

    public void previousMusic() {
        if(this.mixed) {
            this.apiManager.skipPreviousTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.PREVIOUS));
        }
    }

    public void playMusic() {
        if(this.mixed) {
            this.apiManager.playPauseTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.PLAY)); //TODO PAUSE
        }
    }

    public void nextMusic() {
        if(this.mixed) {
            this.apiManager.skipNextTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.NEXT));
        }
    }
    private LogItem createItem(LogItem.LogAction action){
        return new LogItem(this.currentuser.username, action, "null", this.localPlaylist.getCurrentTrack().title, this.algo.getName()); //TODO know current track
    }
}
