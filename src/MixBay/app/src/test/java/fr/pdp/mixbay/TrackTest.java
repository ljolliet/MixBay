package fr.pdp.mixbay;



import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import fr.pdp.mixbay.models.Playlist;
import fr.pdp.mixbay.models.Track;
import fr.pdp.mixbay.models.TrackFeatures;


import static org.junit.Assert.*;

public class TrackTest {

    @Test
    public void equalTest(){
        Track track = new Track("123", "Let it Be", "Let it Be", "The Beatles", "random_url", null);
        Track track2 = new Track("123", "Get Back", "Let it Be", "The Beatles", "random_url", null);
        Track track3 = new Track("12", "Let it Be", "Let it Be", "The Beatles", "random_url", null);
        Playlist playlist = new Playlist("123", "rock");

        assertNotEquals(null, track);
        assertEquals(track, track2);
        assertNotEquals(track, track3);
        assertNotEquals(track, playlist);

    }

    @Test
    public void secondConstructorTest(){
        Track track = new Track("123", "Let it Be", "Let it Be", "The Beatles", "random_url", null);
        Set <String> artists = new HashSet<>();
        artists.add("The Beatles");
        Track track2 = new Track("123", "Let it Be", "Let it Be", artists, "random_url", null);

        assertEquals(track.artists, track2.artists);
    }

}