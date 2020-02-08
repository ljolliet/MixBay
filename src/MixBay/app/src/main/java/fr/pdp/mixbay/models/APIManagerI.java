package fr.pdp.mixbay.models;

import android.content.Context;
import java.util.Set;


public interface APIManagerI {

    public boolean connect(Context context);
    public boolean disconnect();
    public User getUser(String id);
    public Set<Playlist> getUserPlaylist(String id);
    public void playCurrentTrack();
    public void playTrack(String id);

}
