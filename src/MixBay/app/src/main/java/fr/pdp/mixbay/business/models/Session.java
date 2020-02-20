package fr.pdp.mixbay.business.models;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import fr.pdp.mixbay.application.Services;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.LogManagerI;


public class Session {

    private Playlist localePlaylist;
    private User mainUser;
    private User currentuser;
    private Set<User> users;
    private AlgoI algo;
    private LogManagerI logManager;
    private APIManagerI apiManager;

    public Session(APIManagerI api) {
        this.apiManager = api;
        this.localePlaylist = new Playlist("01", "MixBay Playlist");
        this.users = new HashSet<>();
        this.algo = new FirstAlgo();

    }

    public void start(Context context) {
        apiManager.connect(context);
        Services.randomInit(this);
        mix();
    }

    public void mix(){
        localePlaylist = algo.compute(this.users);
    }


    public boolean end() {
        return apiManager.disconnect();
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
        this.addUser(mainUser);
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
}
