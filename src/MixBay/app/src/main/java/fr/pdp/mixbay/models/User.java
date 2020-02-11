package fr.pdp.mixbay.models;

import java.util.HashSet;
import java.util.Set;

public class User {

    public final String id;
    public final String username;

    private Set<Playlist> personnalPlaylist = new HashSet<>();
    private boolean mute;

    /**
     * User loaded from a Streaming service API.
     * @param id Streaming service user id.
     * @param username Streaming service user name.
     */
    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.mute = false;
    }

    /**
     * Add a playlist to users playlists.
     * @param playlist Playlist loaded from a Streaming service API.
     */
    public void addPlaylist(Playlist playlist){
        personnalPlaylist.add(playlist);
    }

    /**
     * @return A copy of the user playlists.
     */
    public Set<Playlist> getPlaylist(){
        return new HashSet<>(this.personnalPlaylist);
    }

    /**
     * @return True if the user is muted.
     */
    public boolean isMute() {
        return this.mute;
    }

    public void mute(){
        this.mute = true;
    }

    public void unmute(){
        this.mute = false;
    }
}
