package fr.pdp.mixbay.models;

import java.util.HashSet;
import java.util.Set;

public class User {

    public final String id;
    public final String username;

    private Set<Playlist> personnalPlaylist = new HashSet<>();

    private boolean mute;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.mute = false;
    }

    public void addPlaylist(Playlist playlist){
        personnalPlaylist.add(playlist);
    }

    public Set<Playlist> getPlaylist(){
        return new HashSet<>(this.personnalPlaylist);
    }

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
