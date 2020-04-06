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
import fr.pdp.mixbay.business.algorithms.MostPleasure;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import static org.junit.Assert.*;

/**
 * Requires to comment the line 43 (generateColor();) of the class business.models.User to work
 */
public class MostPleasureTest extends AlgoTest {
    @Test
    public void MostPleasureTest() {
        MostPleasure MostPleasure = new MostPleasure();
        LocalPlaylist playlist = MostPleasure.computeLocalePlaylist(scoreMap, users, trackList);
        LocalPlaylist shouldBePlaylist = new LocalPlaylist();

        shouldBePlaylist.addTrack(t21);
        shouldBePlaylist.addTrack(t5);
        shouldBePlaylist.addTrack(t29);
        shouldBePlaylist.addTrack(t3);
        shouldBePlaylist.addTrack(t33);
        shouldBePlaylist.addTrack(t45);
        shouldBePlaylist.addTrack(t11);
        shouldBePlaylist.addTrack(t36);
        shouldBePlaylist.addTrack(t47);
        shouldBePlaylist.addTrack(t49);
        shouldBePlaylist.addTrack(t44);
        shouldBePlaylist.addTrack(t30);
        shouldBePlaylist.addTrack(t43);
        shouldBePlaylist.addTrack(t15);
        shouldBePlaylist.addTrack(t35);
        shouldBePlaylist.addTrack(t19);
        shouldBePlaylist.addTrack(t6);
        shouldBePlaylist.addTrack(t26);
        shouldBePlaylist.addTrack(t16);
        shouldBePlaylist.addTrack(t2);
        shouldBePlaylist.addTrack(t27);
        shouldBePlaylist.addTrack(t8);
        shouldBePlaylist.addTrack(t1);
        shouldBePlaylist.addTrack(t12);
        shouldBePlaylist.addTrack(t32);

        assertTrue(playlist.equals(shouldBePlaylist));
    }

}
