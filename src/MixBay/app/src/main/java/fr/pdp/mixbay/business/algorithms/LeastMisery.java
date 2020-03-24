/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.algorithms;

import android.content.Context;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.utils.MapUtil;

public class LeastMisery extends AlgoSuperclass {

    @Override
    public String getName(Context context) {
        return context.getString(R.string.algo_least_misery);
    }

    /**
     * Compute a local playlist of at most Playlist.SIZE_MAX (25) tracks
     * The music score of a track for the group is the lowest score of all
     * the users (Least Misery Strategy).
     * The 25 tracks with the lowest distance are chosen to be part of
     * the locale playlist.
     *
     * @param musicScorePerUser A structure containing the computed personnal
     *                          score of each track for each user
     * @param users             A set of users with datas loaded from a
     *                          streaming API
     * @param tracksList        A set of all the tracks extracted from all
     *                          the playlists
     * @return A playlist of at most 25 tracks
     */
    public LocalPlaylist computeLocalePlaylist(Map<String,
            Map<String, Double>> musicScorePerUser, Set<User> users,
                                               Map<String, Track> tracksList) {
        Map<String, Double> leastMiseryComputed = new LinkedHashMap<>();
        Iterator tracksIt = tracksList.entrySet().iterator();
        while (tracksIt.hasNext()) {
            Map.Entry trackEntry = (Map.Entry) tracksIt.next();
            Track t = (Track) trackEntry.getValue();
            Double leastScore = 0.;
            for (User u : users) {
                if ((musicScorePerUser.get(u.id)).get(t.id) > leastScore) {
                    leastScore = (musicScorePerUser.get(u.id)).get(t.id);
                }
            }
            leastMiseryComputed.put(t.id, leastScore);
        }
        leastMiseryComputed = MapUtil.sortByValue(leastMiseryComputed);
        Iterator computedTracksIterator = leastMiseryComputed.entrySet()
                                                             .iterator();
        int index = 0;
        LocalPlaylist p = new LocalPlaylist();
        while (computedTracksIterator.hasNext()
                && index < LocalPlaylist.SIZE_MAX) {
            Map.Entry entryTrack = (Map.Entry) computedTracksIterator.next();
            p.addTrack(tracksList.get(entryTrack.getKey()));
            index++;
        }
        return p;
    }
}
