package fr.pdp.mixbay.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Playlist {

    public static final int SIZE_MAX = 25;
    public final String id;
    public final String name;

    private Set<Track> tracks = new HashSet<>();


    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTrack(Track track){
        this.tracks.add(track);
    }

    public void addTracks(Set<Track> tracks){
        this.tracks.addAll(tracks);
    }

    public Set<Track> getTracks() {
        return new HashSet<>(tracks);
    }

    /**
     * Equal based on playlist's id.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(o instanceof Playlist) {
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
                ", nbTracks='" + tracks.size()+ '\'' +'}';
    }
}
