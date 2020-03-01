package fr.pdp.mixbay.business.models;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.algorithms.LeastMisery;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.LogManagerI;
import fr.pdp.mixbay.business.exceptions.SessionManagementException;
import fr.pdp.mixbay.data.JSONLogManager;


public class Session {

    private LocalPlaylist localPlaylist;
    private User currentUser;
    private Set<User> users;
    private AlgoI algo;
    private LogManagerI logManager;
    private APIManagerI apiManager;
    private boolean mixed;
    private Context context;


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
     */
    public void start(Context context) {
        this.context = context;
        apiManager.connect(context);
    }

    /**
     * Generates the local playlist with then current algorithm.
     * @return The playlist generated.
     */
    public LocalPlaylist generatePlaylist(){
        this.localPlaylist = algo.compute(this.users);
        this.mixed = true;
        Log.d("Session", "Playlist generated");
        return localPlaylist;
    }

    public void launchPlaylist() {
       /* for(Track t : this.localPlaylist.getTracks()){
            this.apiManager.queueTrack(t.id);
            Log.d("Session Launch Playlist", t.title);
        }*/
        this.apiManager.playTrack(this.localPlaylist.getCurrentTrack().id);
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
            //TODO dec or not dec
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
            //this.apiManager.skipNextTrack();

            this.logManager.append(this.createItem(LogItem.LogAction.NEXT));
            this.localPlaylist.incTrack();
            this.apiManager.playTrack(localPlaylist.getCurrentTrack().id);
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
        return new LogItem(this.currentUser.username, action, "null", this.localPlaylist.getCurrentTrack().title, this.algo.getName());
    }

    /**
     * Create the logFile with the current implementation.
     */
    public void createLogFile() {
        this.logManager.create();
    }

    public void setCurrentUser(User currentuser) {
        this.currentUser = currentuser;
    }

    public void setAlgo(AlgoI algo) {
        this.algo = algo;
    }

    public void setLogManager(LogManagerI logManager) {
        this.logManager = logManager;
    }

    public void addUser(User user) {
        this.users.add(user);
        if(this.currentUser == null)
            this.setCurrentUser(user);
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public APIManagerI getApi() {
        return apiManager;
    }

    public Track getCurrentTrack() {
        return this.localPlaylist.getCurrentTrack();
    }



    public void testIfUserExists(String id) throws SessionManagementException {
        for (User user : users) {
            if (user.id.equals(id))
                throw new SessionManagementException(context.getString(R.string.user_already_added));
        }
    }
}
