package fr.pdp.mixbay;


import org.junit.Test;

import fr.pdp.mixbay.models.Playlist;
import fr.pdp.mixbay.models.Track;

import static org.junit.Assert.*;

public class PlaylistTest {

    @Test
    public void noDuplicateTracks(){
        Track track = new Track("123", "Let it Be", "Let it Be", "The Beatles", "random_url", null);
        Track track2 = new Track("123", "Get Back", "Let it Be", "The Beatles", "random_url", null);
        Playlist playlist = new Playlist("456", "rock");
        playlist.addTrack(track);
        playlist.addTrack(track);
        playlist.addTrack(track2);
        assertEquals(1,playlist.getTracks().size());
    }

    @Test
    public void checkEncapsulation(){
        Track track = new Track("123", "Let it Be", "Let it Be", "The Beatles", "random_url", null);
        Track track2 = new Track("456", "Get Back", "Let it Be", "The Beatles", "random_url", null);
        Playlist playlist = new Playlist("789", "rock");
        playlist.addTrack(track);
        playlist.getTracks().add(track2);
        assertEquals(1, playlist.getTracks().size());
    }
}