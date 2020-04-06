/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Track;
import static org.junit.Assert.*;

public class PlaylistTest {

    @Test
    public void noDuplicateTracks(){
        Set<String> artists = new HashSet<>();
        artists.add("The Beatles");
        Track track = new Track("123", "Let it Be", "Let it Be", artists,
                "random_url");
        Track track2 = new Track("123", "Get Back", "Let it Be", artists,
                "random_url");
        Playlist playlist = new Playlist("456", "rock");
        playlist.addTrack(track);
        playlist.addTrack(track);
        playlist.addTrack(track2);
        assertEquals(1,playlist.getTracks().size());
    }

    @Test
    public void checkEncapsulation(){
        Set<String> artists = new HashSet<>();
        artists.add("The Beatles");
        Track track = new Track("123", "Let it Be", "Let it Be", artists,
                "random_url");
        Track track2 = new Track("456", "Get Back", "Let it Be", artists,
                "random_url");
        Playlist playlist = new Playlist("789", "rock");
        playlist.addTrack(track);
        playlist.getTracks().add(track2);
        assertEquals(1, playlist.getTracks().size());
    }
}