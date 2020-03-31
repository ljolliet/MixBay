/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.pdp.mixbay.business.exceptions.PlayerException;

public class LocalPlaylist implements PlaylistI {

    public static final int SIZE_MAX = 25;
    private int currentTrackIndex = 0;
    private List<Track> tracks = new ArrayList<>();

    /**
     * @return The track the player is currently playing
     */
    public Track getCurrentTrack() {
        return tracks.size() != 0 ? tracks.get(currentTrackIndex) : null;
    }

    /**
     @return The track next track the player will be playing
     */
    public Track getNextTrack() {
        return tracks.size() > currentTrackIndex + 1
                ? tracks.get(currentTrackIndex + 1) : null;
    }

    /**
     * Set the given track as currentTrack.
     *
     * @param id Track's id
     * @throws PlayerException If the given track is not contained in the
     * playlist tracks.
     */
    public void setCurrentTrack(String id) {
        boolean contained = false;
        for (Track t : this.tracks)
            if (id.equals(t.id)) {
                this.currentTrackIndex = tracks.indexOf(t);
                contained = true;
            }
        if (!contained)
            throw new PlayerException("Current track not found in Local " +
                    "Playlist");
    }

    /**
     * Set the current Track as the next Track unless there is no more track
     * after this one.
     * @return true if the incrementation is a success, false otherwise
     */
    public boolean incTrack() {
        if (this.currentTrackIndex < this.tracks.size() - 1)
            this.currentTrackIndex++;
        else return false;
        if (this.tracks.get(currentTrackIndex) == null)
            throw new PlayerException("Current track not found in Local " +
                    "Playlist");
        return true;
    }

    /**
     * Set the previous Track as the next Track unless there is no more track
     * before this one.
     */
    public void decTrack() {
        if (this.currentTrackIndex > 0)
            this.currentTrackIndex--;
        if (this.tracks.get(currentTrackIndex) == null)
            throw new PlayerException("Current track not found in Local " +
                    "Playlist");
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
        return new ArrayList<>(this.tracks);
    }

    public void restartPlaylist() {
        this.currentTrackIndex = 0;
    }

    private boolean contains(Track trackContained){
        for (Track t : tracks) {
            if (t.equals(trackContained)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalPlaylist that = (LocalPlaylist) o;
        if (that.getTracks().size() != this.tracks.size()) return false;
        for (Track t : this.getTracks()) {
            if (!that.contains(t)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracks);
    }
}
