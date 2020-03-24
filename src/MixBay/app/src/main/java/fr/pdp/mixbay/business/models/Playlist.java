/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Playlist implements PlaylistI {

    public final String id;
    public final String name;
    private Set<Track> tracks = new HashSet<>();


    /**
     * Create an empty Playlist with the given information.
     *
     * @param id   Playlist's id on the streaming platform.
     * @param name Playlist's name on the streaming platform.
     */
    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    @Override
    public void addTracks(Set<Track> tracks) {
        this.tracks.addAll(tracks);
    }

    @Override
    public Collection<Track> getTracks() {
        return new HashSet<>(tracks);
    }

    /**
     * Equal based on playlist's id.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Playlist) {
            Playlist playlist = (Playlist) o;
            return this.id.equals(playlist.id);
        }
        return false;
    }

    /**
     * HashCode based on playlist's id.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nbTracks='" + tracks.size() + '\'' + '}';
    }
}
