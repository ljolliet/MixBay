package fr.pdp.mixbay.business.models;

import java.util.Collection;
import java.util.Set;

interface PlaylistI {

    void addTrack(Track track);
    void addTracks(Set<Track> tracks);
    Collection<Track> getTracks();
}
