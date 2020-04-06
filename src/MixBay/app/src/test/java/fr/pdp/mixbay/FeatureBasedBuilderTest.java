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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import fr.pdp.mixbay.business.algorithms.FeatureBasedBuilder;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.TrackFeatures;
import fr.pdp.mixbay.business.models.User;

import static org.junit.Assert.*;

/**
 * Requires to comment the line 43 (generateColor();) of the class business.models.User to work
 */
public class FeatureBasedBuilderTest {

    @Test
    public void computeUserProfileTest(){
        User u1 = new User("1","1");
        User u2 = new User("2","2");
        User u3 = new User("3","3");
        Set<User> users = new HashSet<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        Playlist p1 = new Playlist("1","1");
        Playlist p2 = new Playlist("2","2");
        Playlist p3 = new Playlist("3","3");
        Playlist p4 = new Playlist("4","4");
        Playlist p5 = new Playlist("5","5");
        Track t1 = new Track("1","1",null, null, null);
        Track t2 = new Track("2","2",null, null, null);
        Track t3 = new Track("3","3",null, null, null);
        Track t4 = new Track("4","4",null, null, null);
        Track t5 = new Track("5","5",null, null, null);
        Track t6 = new Track("6","6",null, null, null);
        Track t7 = new Track("7","7",null, null, null);
        Track t8 = new Track("8","8",null, null, null);
        Track t9 = new Track("9","9",null, null, null);
        Track t10 = new Track("10","10",null, null, null);
        Set<Track> trackList = new HashSet<>();
        trackList.add(t1);
        trackList.add(t2);
        trackList.add(t3);
        trackList.add(t4);
        trackList.add(t5);
        trackList.add(t6);
        trackList.add(t7);
        trackList.add(t8);
        trackList.add(t9);
        trackList.add(t10);
        TrackFeatures tf1 = new TrackFeatures(0.9,0.1,0.1,0.5,0.4,0.9,0.4);
        TrackFeatures tf2 = new TrackFeatures(0.1,0.1,0.6,0.7,0.1,0.6,0.5);
        TrackFeatures tf3 = new TrackFeatures(0.2,0.2,0.9,0.5,0.5,0.1,0.9);
        TrackFeatures tf4 = new TrackFeatures(0.5,0.2,0.9,0.1,0.6,0.1,0.9);
        TrackFeatures tf5 = new TrackFeatures(0.6,0.8,0.5,0.2,0.9,0.8,0.8);
        TrackFeatures tf6 = new TrackFeatures(0.6,0.6,0.5,0.2,0.5,0.4,0.5);
        TrackFeatures tf7 = new TrackFeatures(0.4,0.9,0.7,0.8,0.7,0.6,0.4);
        TrackFeatures tf8 = new TrackFeatures(0.7,0.5,0.2,0.9,0.1,0.9,0.7);
        TrackFeatures tf9 = new TrackFeatures(0.6,0.8,0.4,0.1,0.1,0.7,0.3);
        TrackFeatures tf10 = new TrackFeatures(0.8,0.5,0.7,0.4,0.2,0.1,0.1);
        t1.setFeatures(tf1);
        t2.setFeatures(tf2);
        t3.setFeatures(tf3);
        t4.setFeatures(tf4);
        t5.setFeatures(tf5);
        t6.setFeatures(tf6);
        t7.setFeatures(tf7);
        t8.setFeatures(tf8);
        t9.setFeatures(tf9);
        t10.setFeatures(tf10);
        p1.addTrack(t1);
        p1.addTrack(t2);
        p1.addTrack(t3);
        p2.addTrack(t1);
        p2.addTrack(t4);
        p2.addTrack(t5);
        p3.addTrack(t6);
        p3.addTrack(t7);
        p3.addTrack(t3);
        p4.addTrack(t8);
        p4.addTrack(t9);
        p5.addTrack(t10);
        u1.addPlaylist(p1);
        u1.addPlaylist(p5);
        u2.addPlaylist(p2);
        u2.addPlaylist(p3);
        u3.addPlaylist(p4);

        FeatureBasedBuilder score = new FeatureBasedBuilder();

        Map<String, Track> tracksMapUser1 = new HashMap<>();
        TrackFeatures computedTrackFeature = score.computeUserProfile(
                u1, tracksMapUser1);
        TrackFeatures shouldBeTrackFeature = new TrackFeatures(
                0.5,0.225,0.575,0.525,0.3,0.425,0.475);

        assertTrue(computedTrackFeature.equals(shouldBeTrackFeature));
    }

    @Test
    public void computeMusicScorePerUserTest(){
        User u1 = new User("1","1");
        User u2 = new User("2","2");
        User u3 = new User("3","3");
        Set<User> users = new HashSet<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        Playlist p1 = new Playlist("1","1");
        Playlist p2 = new Playlist("2","2");
        Playlist p3 = new Playlist("3","3");
        Playlist p4 = new Playlist("4","4");
        Playlist p5 = new Playlist("5","5");
        Track t1 = new Track("1","1",null, null, null);
        Track t2 = new Track("2","2",null, null, null);
        Track t3 = new Track("3","3",null, null, null);
        Track t4 = new Track("4","4",null, null, null);
        Track t5 = new Track("5","5",null, null, null);
        Track t6 = new Track("6","6",null, null, null);
        Track t7 = new Track("7","7",null, null, null);
        Track t8 = new Track("8","8",null, null, null);
        Track t9 = new Track("9","9",null, null, null);
        Track t10 = new Track("10","10",null, null, null);
        Set<Track> trackList = new HashSet<>();
        trackList.add(t1);
        trackList.add(t2);
        trackList.add(t3);
        trackList.add(t4);
        trackList.add(t5);
        trackList.add(t6);
        trackList.add(t7);
        trackList.add(t8);
        trackList.add(t9);
        trackList.add(t10);
        TrackFeatures tf1 = new TrackFeatures(0.9,0.1,0.1,0.5,0.4,0.9,0.4);
        TrackFeatures tf2 = new TrackFeatures(0.1,0.1,0.6,0.7,0.1,0.6,0.5);
        TrackFeatures tf3 = new TrackFeatures(0.2,0.2,0.9,0.5,0.5,0.1,0.9);
        TrackFeatures tf4 = new TrackFeatures(0.5,0.2,0.9,0.1,0.6,0.1,0.9);
        TrackFeatures tf5 = new TrackFeatures(0.6,0.8,0.5,0.2,0.9,0.8,0.8);
        TrackFeatures tf6 = new TrackFeatures(0.6,0.6,0.5,0.2,0.5,0.4,0.5);
        TrackFeatures tf7 = new TrackFeatures(0.4,0.9,0.7,0.8,0.7,0.6,0.4);
        TrackFeatures tf8 = new TrackFeatures(0.7,0.5,0.2,0.9,0.1,0.9,0.7);
        TrackFeatures tf9 = new TrackFeatures(0.6,0.8,0.4,0.1,0.1,0.7,0.3);
        TrackFeatures tf10 = new TrackFeatures(0.8,0.5,0.7,0.4,0.2,0.1,0.1);
        t1.setFeatures(tf1);
        t2.setFeatures(tf2);
        t3.setFeatures(tf3);
        t4.setFeatures(tf4);
        t5.setFeatures(tf5);
        t6.setFeatures(tf6);
        t7.setFeatures(tf7);
        t8.setFeatures(tf8);
        t9.setFeatures(tf9);
        t10.setFeatures(tf10);
        p1.addTrack(t1);
        p1.addTrack(t2);
        p1.addTrack(t3);
        p2.addTrack(t1);
        p2.addTrack(t4);
        p2.addTrack(t5);
        p3.addTrack(t6);
        p3.addTrack(t7);
        p3.addTrack(t3);
        p4.addTrack(t8);
        p4.addTrack(t9);
        p5.addTrack(t10);
        u1.addPlaylist(p1);
        u1.addPlaylist(p2);
        u2.addPlaylist(p3);
        u2.addPlaylist(p4);
        u3.addPlaylist(p5);

        FeatureBasedBuilder score = new FeatureBasedBuilder();

        Map<String, Track> tracksMapUser1 = new HashMap<>();
        Map<String, Track> tracksMapUser2 = new HashMap<>();
        Map<String, Track> tracksMapUser3 = new HashMap<>();

        TrackFeatures userProfile1 = score.computeUserProfile(u1, tracksMapUser1);
        TrackFeatures userProfile2 = score.computeUserProfile(u2, tracksMapUser2);
        TrackFeatures userProfile3 = score.computeUserProfile(u3, tracksMapUser3);

        Map<String, TrackFeatures> userProfiles = new HashMap<>();
        userProfiles.put(u1.id, userProfile1);
        userProfiles.put(u2.id, userProfile2);
        userProfiles.put(u3.id, userProfile3);

        HashMap<String, Map<String, Double>> computedMusicScorePerUser = score.computeMusicScorePerUser(userProfiles);

        double t1ScoreForUser1 = 0.801950746617272;
        double TRESHOLD = Math.sqrt(TrackFeatures.SIZE*(
                FeatureBasedBuilder.RANDOM_RATIO*FeatureBasedBuilder.RANDOM_RATIO));
        // needed since there is a random factor in computeMusicScorePerUser
        double computedt1ScoreForUser1 = computedMusicScorePerUser.get("1").get("1");

        assertTrue(Math.abs(computedt1ScoreForUser1 - t1ScoreForUser1) < TRESHOLD);
    }
}
