package fr.pdp.mixbay.business.models;

import android.graphics.Color;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class User {

    private static int ID_GENERATOR = 0;

    public final String id;
    public final String username;
    public final String anonymousUsername;

    private Set<Playlist> personalPlaylists = new HashSet<>();
    private boolean mute;

    private int color;
    public final char initial;

    /**
     * User loaded from a Streaming service API.
     * @param id Streaming service user id.
     * @param username Streaming service user name.
     */
    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.mute = false;
        this.initial = this.username.charAt(0);
        this.anonymousUsername = "user " + ID_GENERATOR++;
        generateColor();
    }

    /**
     * Generate user's color
     */
    private void generateColor() {
        Random rand = new Random();

        // Generate "light" colors - from https://stackoverflow.com/a/4246418
        int r = rand.nextInt() / 2 + 128;
        int g = rand.nextInt() / 2 + 128;
        int b = rand.nextInt() / 2 + 128;
        color = Color.rgb(r, g, b);
    }

    /**
     * Add a playlist to users playlists.
     * @param playlist Playlist loaded from a Streaming service API.
     */
    public void addPlaylist(Playlist playlist){
        personalPlaylists.add(playlist);
    }

    /**
     * Add a Collection of playlists to the user.
     * @param playlists A collection of Playlists
     */
    public void addAllPlaylists(Collection<Playlist> playlists) {
        personalPlaylists.addAll(playlists);
    }

    /**
     * @return A copy of the user playlists.
     */
    // TODO Rename into getAllPlaylists
    public Set<Playlist> getPlaylist(){
        return new HashSet<>(this.personalPlaylists);
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

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof User))
            return false;

        User user = ((User) obj);
        return this.id.equals(user.id);
    }
}
