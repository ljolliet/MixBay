package fr.pdp.mixbay.business.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.business.models.AlgoI;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.ScoreBuilderI;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;

abstract class AlgoSuperclass implements AlgoI {
    ScoreBuilderI score = new FeatureBasedBuilder();

    @Override
    public LocalPlaylist compute(Set<User> users) {
        Set<User> unmutedUsers = new HashSet<>();
        for (User u : users) {
            if (!u.isMute()) {
                unmutedUsers.add(u);
            }
        }
        Map<String, Track> tracksList = new HashMap<>();
        return computeLocalePlaylist(score.compute(unmutedUsers, tracksList), unmutedUsers, tracksList);
    }

    public abstract LocalPlaylist computeLocalePlaylist(Map<String, Map<String, Double>> musicScorePerUser, Set<User> users, Map<String, Track> tracksList);
}
