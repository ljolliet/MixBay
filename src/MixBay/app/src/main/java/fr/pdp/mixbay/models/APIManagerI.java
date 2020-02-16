package fr.pdp.mixbay.models;

import android.content.Context;
import java.util.Set;
import java.util.concurrent.Future;


public interface APIManagerI {

    int SPOTIFY_REQUEST_CODE = 1337;

    /**
     * Authenticate the main user to the API.
     * @return true if the user is connected after authentication.
     */
    boolean connect(Context context);

    /**
     * TODO Paul
     * @return
     */
    boolean disconnect();

    /**
     * TODO @paul
     * @param id
     * @return
     */
    Future<User> getUser(String id);

    /**
     * TODO @aul
     * @param userId
     * @return
     */
    Future<Set<Playlist>> getUserPlaylists(String userId);


    /**
     * Play the given track.
     * @param id track's id to play.
     */
    void playTrack(String id);

    /**
     * Play/Pause the current track : play if paused and pause otherwise
     */
    void playPauseTrack();

    /**
     * Add the given track to the user's queue.
     * @param id tr'ack's id to queue.
     */
    void queueTrack(String id);

    /**
     * Skips to next track in the user’s queue.
     */
    void skipNextTrack();

    /**
     * Skips to previous track in the user’s queue.
     */
    void skipPreviousTrack();


    /**
     * TODO
     */
    void toggleRepeat();
    void toggleShuffle();
    void seekFordward();
    void seekBackward();



    void setAccessToken(String token);

}
