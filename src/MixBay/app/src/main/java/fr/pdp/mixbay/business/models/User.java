/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

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
    public final char initial;
    private Set<Playlist> personalPlaylists = new HashSet<>();
    private boolean mute;
    private int color;

    /**
     * User loaded from a Streaming service API.
     *
     * @param id       Streaming service user id.
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
        final int threshold = 128; // Half of the color range [0, 255]

        // Generate "light" colors - from https://stackoverflow.com/a/4246418
        int r = rand.nextInt() / 2 + threshold;
        int g = rand.nextInt() / 2 + threshold;
        int b = rand.nextInt() / 2 + threshold;
        color = Color.rgb(r, g, b);
    }

    /**
     * Add a playlist to users playlists.
     *
     * @param playlist Playlist loaded from a Streaming service API.
     */
    public void addPlaylist(Playlist playlist) {
        personalPlaylists.add(playlist);
    }

    /**
     * Add a Collection of playlists to the user.
     *
     * @param playlists A collection of Playlists
     */
    public void addAllPlaylists(Collection<Playlist> playlists) {
        personalPlaylists.addAll(playlists);
    }

    /**
     * @return A copy of the user playlists.
     */
    public Set<Playlist> getAllPlaylists() {
        return new HashSet<>(this.personalPlaylists);
    }

    /**
     * @return True if the user is muted.
     */
    public boolean isMute() {
        return this.mute;
    }

    public void mute() {
        this.mute = true;
    }

    public void unmute() {
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
