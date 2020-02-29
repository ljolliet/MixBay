package fr.pdp.mixbay.business.models;

import java.util.Collection;
import java.util.Set;

interface PlaylistI {

    /**
     * A the given Track to the Playlist Collection
     * @param track A track to add.
     */
    void addTrack(Track track);
    /**
     * A the given Tracks to the Playlist Collection
     * @param tracks A Set of Tracks to add.
     */
    void addTracks(Set<Track> tracks);

    /**
     * @return A copy of the Collection of Tracks that constitutes the Playlist.
     */
    Collection<Track> getTracks();
}
