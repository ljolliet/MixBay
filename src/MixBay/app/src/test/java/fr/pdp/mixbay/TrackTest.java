package fr.pdp.mixbay;


import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class TrackTest {

    @Test
    public void equalTest(){
        Set<String> artists = new HashSet<>();
        artists.add("The Beatles");
        Track track = new Track("123", "Let it Be", "Let it Be", artists,
                "random_url");
        Track track2 = new Track("123", "Get Back", "Let it Be", artists,
                "random_url");
        Track track3 = new Track("12", "Let it Be", "Let it Be", artists,
                "random_url");
        Playlist playlist = new Playlist("123", "rock");

        assertNotEquals(null, track);
        assertEquals(track, track2);
        assertNotEquals(track, track3);
        assertNotEquals(track, playlist);

    }

    @Test
    public void secondConstructorTest(){
        Set <String> artists = new HashSet<>();
        artists.add("The Beatles");
        Track track = new Track("123", "Let it Be", "Let it Be", artists,
                "random_url");
        Track track2 = new Track("123", "Let it Be", "Let it Be", artists,
                "random_url");

        assertEquals(track.artists, track2.artists);
    }

}