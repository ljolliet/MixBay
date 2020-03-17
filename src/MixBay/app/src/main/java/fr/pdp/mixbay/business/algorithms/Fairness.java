package fr.pdp.mixbay.business.algorithms;

import android.content.Context;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;

public class Fairness extends AlgoSuperclass{

    @Override
    public String getName(Context context) {
//        return this.getClass().toString() + " with " + score.getClass().toString();
        return context.getString(R.string.algo_fairness);
    }

    @Override
    public LocalPlaylist computeLocalePlaylist(Map<String, Map<String, Double>> musicScorePerUser, Set<User> users, Map<String, Track> tracksList) {
        LocalPlaylist p = new LocalPlaylist();
        int index = 0;
        boolean empty = false;
        while(index < LocalPlaylist.SIZE_MAX && !empty) {
            Iterator usersIt = users.iterator();
            while (usersIt.hasNext() && index++ < LocalPlaylist.SIZE_MAX && !empty) {
                User currentUser = (User) usersIt.next();
                Iterator tracksIt = musicScorePerUser.get(currentUser.id).entrySet().iterator();
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
