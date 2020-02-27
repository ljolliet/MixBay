package fr.pdp.mixbay.business.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.pdp.mixbay.business.exceptions.PlayerException;

public class LocalPlaylist implements PlaylistI {

    public static final int SIZE_MAX = 25;
    private Track currentTrack;
    private List<Track> tracks = new ArrayList<>();


    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(String id){
        this.currentTrack = null;
        for(Track t : this.tracks)
            if(id.equals(t.id))
                this.currentTrack = t;
        if (this.currentTrack == null)
            throw new PlayerException("Current track not found in Local Playlist");
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
}
