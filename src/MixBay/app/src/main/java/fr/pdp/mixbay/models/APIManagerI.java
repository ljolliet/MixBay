package fr.pdp.mixbay.models;

import android.content.Context;
import java.util.Set;


public interface APIManagerI {

    int SPOTIFY_REQUEST_CODE = 1337;

    boolean connect(Context context);
    boolean disconnect();
    User getUser(String id);
    Set<Playlist> getUserPlaylist(String id);
    void playCurrentTrack();
    void playTrack(String id);
    void setAccessToken(String token);

}
