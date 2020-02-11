package fr.pdp.mixbay.models;

import android.content.Context;
import java.util.Set;
import java.util.concurrent.Future;


public interface APIManagerI {

    int SPOTIFY_REQUEST_CODE = 1337;

    boolean connect(Context context);
    boolean disconnect();
    Future<User> getUser(String id);
    Future<Set<Playlist>> getUserPlaylists(String userId);
    void playCurrentTrack();
    void skipNextTrack();
    void skipPreviousTrack();
    void playTrack(String id);
    void setAccessToken(String token);

}
