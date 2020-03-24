/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;

public class Factory {

    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Track> tracks = new HashMap<>();
    private static Map<Track, Set<User>> usersForTracks = new HashMap<>();

    public static User getUser(String id, String username) {
        if (users.containsKey(id))
            return users.get(id);

        User user = new User(id, username);
        users.put(id, user);
        return user;
    }

    public static Track getTrack(String id, String title, String album,
                                 Set<String> artists, String cover_url,
                                 String userId) {
        if (tracks.containsKey(id)) {
            addUserForTracks(tracks.get(id), users.get(userId));
            return tracks.get(id);
        }

        Track track = new Track(id, title, album, artists, cover_url);
        tracks.put(id, track);

        addUserForTracks(track, users.get(userId));

        return track;
    }

    public static Set<User> getUsersForTrack(Track track) {
        return usersForTracks.get(track);
    }

    private static void addUserForTracks(Track track, User user) {
        if (!usersForTracks.containsKey(track))
            usersForTracks.put(track, new HashSet<>());

        usersForTracks.get(track).add(user); // NullPointerException?
    }

}
