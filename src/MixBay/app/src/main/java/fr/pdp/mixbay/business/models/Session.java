/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.algorithms.LeastMisery;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.LogManagerI;
import fr.pdp.mixbay.business.exceptions.PlayerException;
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
    private boolean paused;
    private Context context;


    public Session(APIManagerI api) {
        this.apiManager = api;
        this.localPlaylist = new LocalPlaylist();
        this.users = new HashSet<>();
        this.algo = new LeastMisery();
        this.logManager = new JSONLogManager();
        this.mixed = false;
        this.paused = false;
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
     *
     * @return The playlist generated.
     */
    public LocalPlaylist generatePlaylist() {
        this.localPlaylist = algo.compute(this.users);
        if (this.localPlaylist.getTracks().size() > 0)
            this.mixed = true;
        Log.d("Session", "Playlist generated");
        return localPlaylist;
    }

    public void launchPlaylist() {
        this.apiManager.emptyQueue();

        if (this.localPlaylist.getCurrentTrack() == null)
            throw new PlayerException(
                    context.getString(R.string.playlist_empty_error));

        this.apiManager.playTrack(this.localPlaylist.getCurrentTrack().id);

        if (this.localPlaylist.getNextTrack() != null)
            this.apiManager.queueTrack(localPlaylist.getNextTrack().id);
    }

    /**
     * Finish properly the session.
     *
     * @return true if the disconnection worked normally
     */
    public boolean finish() {
        return apiManager.disconnect();
    }

    /**
     * Play the current track on the local playlist.
     */
    public void playMusic() {
        if (this.mixed) {
            this.apiManager.resumeTrack();
            this.paused = false;
            this.logManager.append(this.createItem(LogItem.LogAction.PLAY));
        }
    }

    public void pauseMusic() {
        if (this.mixed) {
            this.apiManager.pauseTrack();
            this.paused = true;
            this.logManager.append(this.createItem(LogItem.LogAction.PAUSE));
        }
    }

    /**
     * Skip the the beginning of the track (or skip to previous track,
     * depending on the API).
     */
    public void previousMusic() {
        if (this.mixed) {
            this.localPlaylist.decTrack();
            this.apiManager.emptyQueue();
            this.apiManager.playTrack(localPlaylist.getCurrentTrack().id);
            this.apiManager.queueTrack(localPlaylist.getNextTrack().id);
            this.logManager.append(this.createItem(LogItem.LogAction.PREVIOUS));
            }
    }

    /**
     * Skip to the next Track on the local playlist.
     */
    public void nextMusic() {
        if (this.mixed) {
            if(!this.localPlaylist.incTrack())  //end of the playlist
                this.localPlaylist.restartPlaylist();

            if (this.localPlaylist.getNextTrack() != null) {
                this.apiManager.queueTrack(localPlaylist.getNextTrack().id);
            }
            this.apiManager.skipNextTrack();
            this.logManager.append(this.createItem(LogItem.LogAction.NEXT));
        }
    }

    /**
     * Like the current Track of the local playlist.
     */
    public void likeCurrentTrack() {
        if (this.mixed) {
            this.logManager.append(this.createItem(LogItem.LogAction.LIKE));
            this.getCurrentTrack().like();
        }
    }

    public void unlikeCurrentTrack() {
        if (this.mixed) {
            this.logManager.append(this.createItem(LogItem.LogAction.UNLIKE));
            this.getCurrentTrack().unlike();
        }
    }

    /**
     * Creates a log item with current user, current track, current algorithm
     * and given action.
     *
     * @param action Tha action performed by the user.
     * @return The log item created.
     */
    private LogItem createItem(LogItem.LogAction action) {
        return new LogItem(this.currentUser.anonymousUsername, action,
                "null", this.localPlaylist.getCurrentTrack().title,
                this.algo.getName(context));
    }

    /**
     * Create the logFile with the current implementation.
     */
    public void createLogFile() {
        this.logManager.create();
    }

    public AlgoI getAlgo() {
        return algo;
    }

    public void setAlgo(AlgoI algo) {
        this.algo = algo;
    }

    public void setLogManager(LogManagerI logManager) {
        this.logManager = logManager;
    }

    public void addUser(User user) {
        this.users.add(user);
        if (this.currentUser == null)
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
                throw new SessionManagementException(context
                        .getString(R.string.user_already_added));
        }
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User currentuser) {
        this.currentUser = currentuser;
    }

    public void syncCurrentTrack(String id) {
        if (isMixed()) {
            Log.d("Session", "Checking id : " + id + "| current id : "
                    + localPlaylist.getCurrentTrack().id);
            // if player not synchronized to the API
            if (!id.equals(localPlaylist.getCurrentTrack().id))
                if (id.equals(localPlaylist.getNextTrack().id)) {
                    localPlaylist.incTrack();
                    apiManager.queueTrack(localPlaylist.getNextTrack().id);
                    Log.d("Session", "Sync to next track");
                } /*else
                    for (Track t : localPlaylist.getTracks())
                        if (id.equals(t.id)) {
                            localPlaylist.setCurrentTrack(t.id);
                            apiManager.emptyQueue();
                            apiManager.queueTrack(localPlaylist.getNextTrack().id);
                            Log.d("Session", "Sync to a track in current Playlist");
                            break;
                        }*/
        }
    }

    public boolean isMixed() {
        return mixed;
    }

    public boolean isPaused() {
        return paused;
    }
}
