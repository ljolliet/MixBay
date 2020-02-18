package fr.pdp.mixbay.business.dataAccess;

import android.content.Context;
import java.util.Set;
import java.util.concurrent.Future;

import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.User;


public interface APIManagerI {

    int SPOTIFY_REQUEST_CODE = 1337;

    /**
     * Authenticate the main user to the API.
     * @return true if the user is connected after authentication.
     */
    boolean connect(Context context);

    /**
     * Disconnect the main user to the API.
     * @return true if disconnected.
     */
    boolean disconnect();

    /**
     * Get the User with the given id.
     * @param id The id of the user.
     * @return A Future with the User.
     */
    Future<User> getUser(String id);

    /**
     * Get all the Playlists of a user.
     * @param userId The id of the user.
     * @return A Future with the set of Playlists.
     */
    Future<Set<Playlist>> getUserPlaylists(String userId);


    /**
     * Play the given track.
     * @param id track's id to play.
     */
    void playTrack(String id);

    /**
     * Play/Pause the current track: play if paused and pause otherwise
     */
    void playPauseTrack();

    /**
     * Add the given track to the user's queue.
     * @param id track's id to queue.
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

    /**
     * Set the access access token of the main user to allow API requests.
     * @param token The token.
     */
    void setAccessToken(String token);

}
