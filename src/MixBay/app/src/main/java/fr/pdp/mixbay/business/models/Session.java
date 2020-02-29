package fr.pdp.mixbay.business.models;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

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

    /**
     * Start/Initialise the session.
     * @return true if the initialisation worked normally
     */
    public void start(Context context) {
        apiManager.connect(context);
    }

    /**
     * Generates the local playlist with then current algorithm.
     * @return The playlist generated.
     */
    public LocalPlaylist generatePlaylist(){
        localPlaylist = algo.compute(this.users);
        this.mixed = true;
        Log.d("Session", "Playlist generated");
        return localPlaylist;
    }

    /**
     * Finish properly the session.
     * @return true if the disconnection worked normally
     */
    public boolean finish() {
        return apiManager.disconnect();
    }

    /**
     * Skip the the beginning of the track (or skip to previous track, depending on the API).
     */
    public void previousMusic() {
        if(this.mixed) {
            this.apiManager.skipPreviousTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.PREVIOUS));
        }
        Log.d("Session", "Action 'Previous' impossible : Playlist not generated");
    }

    /**
     * Play the current track on the local playlist.
     */
    public void playMusic() {
        if(this.mixed) {
            this.apiManager.playPauseTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.PLAY)); //TODO PAUSE
        }
        else
            Log.d("Session", "Action 'Play' impossible : Playlist not generated");
    }

    /**
     * Skip to the next Track on the local playlist.
     */
    public void nextMusic() {
        if(this.mixed) {
            this.apiManager.skipNextTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.NEXT));
        }
        else
            Log.d("Session", "Action 'Next' impossible : Playlist not generated");
    }

    /**
     *  Creates a log item with current user, current track, current algorithm and given action.
     * @param action Tha action performed by the user.
     * @return The log item created.
     */
    private LogItem createItem(LogItem.LogAction action){
        return new LogItem(this.currentuser.username, action, "null", this.localPlaylist.getCurrentTrack().title, this.algo.getName());
    }

    /**
     * Create the logFile with the current implementation.
     */
    public void createLogFile() {
        this.logManager.create();
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
        if(this.currentuser == null)
            this.setCurrentUser(user);
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public APIManagerI getApi() {
        return apiManager;
    }
}
