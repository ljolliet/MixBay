package fr.pdp.mixbay.business.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.pdp.mixbay.business.exceptions.PlayerException;

public class LocalPlaylist implements PlaylistI {

    public static final int SIZE_MAX = 25;
    private int currentTrackIndex = 0;
    private List<Track> tracks = new ArrayList<>();

    /**
     * TODO
     * @return
     */
    public Track getCurrentTrack() {
        return tracks.size() != 0 ? tracks.get(currentTrackIndex) : null;
    }

    public void incTrack(){
        this.currentTrackIndex++;
        if (this.tracks.size() < currentTrackIndex || this.tracks.get(currentTrackIndex) == null)
            throw new PlayerException("Current track not found in Local Playlist");
    }

    public void decTrack() {
        if(this.currentTrackIndex > 0)
            this.currentTrackIndex--;
        if (this.tracks.get(currentTrackIndex) == null)
            throw new PlayerException("Current track not found in Local Playlist");
    }

    /**
     * TODO
     * @return
     */
    public Track getNextTrack() {
        return tracks.size() > currentTrackIndex+1 ? tracks.get(currentTrackIndex+1) : null;
    }


    /**
     * Set the given track as currentTrack.
     * @param id Track's id
     * @throws PlayerException If the given track is not contained in the playlist tracks.
     */
    public void setCurrentTrack(String id){
        boolean contained = false;
        for(Track t : this.tracks)
            if(id.equals(t.id)) {
                this.currentTrackIndex = tracks.indexOf(t);
                contained = true;
            }
        if (!contained)
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
