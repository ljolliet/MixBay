package fr.pdp.mixbay.business.models;

import android.graphics.Color;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class User {

    public final String id;
    public final String username;

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
        generateColor();
    }

    /**
     * Generate user's color
     */
    private void generateColor() {
        Random rand = new Random();

        // Generate pastel colors - from https://stackoverflow.com/a/8739276
        final float hue = rand.nextFloat();
        final float saturation = 0.9f; // Brilliant
        final float luminance = 1.0f;  // Bright
        color = Color.HSVToColor(new float[]{hue, saturation, luminance});
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

}
