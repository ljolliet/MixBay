package fr.pdp.mixbay.business.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalPlaylist implements PlaylistI {

    public static final int SIZE_MAX = 25;
    private int currentTrack = 0;
    private List<Track> tracks = new ArrayList<>();


    public Track getCurrentTrack() {
        return this.tracks.get(currentTrack);
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
        return new HashSet<>(this.tracks);
    }
}
