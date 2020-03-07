package fr.pdp.mixbay.business.algorithms;

import java.util.HashMap;
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
        Map<String, Track> tracksList = new HashMap<>();
        return computeLocalePlaylist(score.compute(users, tracksList), users, tracksList);
    }

    public abstract LocalPlaylist computeLocalePlaylist(Map<String, Map<String, Double>> musicScorePerUser, Set<User> users, Map<String, Track> tracksList);
}
