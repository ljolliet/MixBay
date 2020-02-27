package fr.pdp.mixbay.business.algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.business.models.AlgoI;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.ScoreBuilderI;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.utils.MapUtil;

public class LeastMisery implements AlgoI {

    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public LocalPlaylist compute(Set<User> users) {
        Map<String, Track> tracksList = new HashMap<>();
        ScoreBuilderI score = new FeatureBasedBuilder();
        return computeLocalePlaylist(score.compute(users, tracksList), users, tracksList);
    }

    /**
     * Compute a local playlist of Playlist.SIZE_MAX (25) tracks
     * The 25 tracks with the lowest computed score are chosen to be part of the locale playlist (Least Misery Strategy)
     *
     * @param musicScorePerUser A structure containing the computed personnal score of each track for each user
     * @param users A set of users with datas loaded from a streaming API
     * @param tracksList A set of all the tracks extracted from all the playlists
     * @return A playlist of 25 tracks
     */
    public LocalPlaylist computeLocalePlaylist(Map<String, Map<String, Double>> musicScorePerUser, Set<User> users, Map<String, Track> tracksList) {
        Map<String, Double> leastMiseryComputed = new LinkedHashMap<>();
        Iterator tracksIt = tracksList.entrySet().iterator();
        while (tracksIt.hasNext()) {
            Map.Entry trackEntry = (Map.Entry) tracksIt.next();
            Track t = (Track) trackEntry.getValue();
            Double leastScore = 100.;
            for (User u : users) {
                if ((musicScorePerUser.get(u.id)).get(t.id) < leastScore) {
                    leastScore = (musicScorePerUser.get(u.id)).get(t.id);
                }
            }
            leastMiseryComputed.put(t.id, leastScore);
        }
        leastMiseryComputed = MapUtil.sortByValue(leastMiseryComputed);
        Iterator computedTracksIterator = leastMiseryComputed.entrySet().iterator();
        int index = 0;
        LocalPlaylist p = new LocalPlaylist();
        while (computedTracksIterator.hasNext() && index < LocalPlaylist.SIZE_MAX) {
            Map.Entry entryTrack = (Map.Entry) computedTracksIterator.next();
            p.addTrack(tracksList.get(entryTrack.getKey()));
            index++;
        }
        return p;
    }
}
