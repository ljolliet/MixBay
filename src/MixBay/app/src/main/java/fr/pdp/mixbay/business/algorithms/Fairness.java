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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.utils.MapUtil;

public class Fairness extends AlgoSuperclass {

    @Override
    public String getName(Context context) {
        return context.getString(R.string.algo_fairness);
    }

    @Override
    public LocalPlaylist computeLocalePlaylist(Map<String,
            Map<String, Double>> musicScorePerUser, Set<User> users,
                                               Map<String, Track> tracksList) {
        for(User u : users){
            Map<String, Double> sortedScores = MapUtil.sortByValue(musicScorePerUser.get(u.id));
            musicScorePerUser.put(u.id, (sortedScores));
        }
        LocalPlaylist p = new LocalPlaylist();
        int index = 0;
        boolean empty = false;
        while (index < LocalPlaylist.SIZE_MAX && !empty) {
            Iterator usersIt = users.iterator();
            while (usersIt.hasNext() && index++ < LocalPlaylist.SIZE_MAX
                        && !empty) {
                User currentUser = (User) usersIt.next();
                Iterator tracksIt = musicScorePerUser.get(currentUser.id)
                                                     .entrySet().iterator();
                Map.Entry trackEntry = (Map.Entry) tracksIt.next();
                Track t = tracksList.get(trackEntry.getKey());
                p.addTrack(t);
                for (User u : users) {
                    musicScorePerUser.get(u.id).remove(trackEntry.getKey());
                }
                empty = musicScorePerUser.get(currentUser.id).size() == 0;
            }
        }
        return p;
    }
}
