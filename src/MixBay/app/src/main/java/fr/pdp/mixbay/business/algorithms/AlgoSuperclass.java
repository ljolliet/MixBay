/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

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
    private ScoreBuilderI score = new FeatureBasedBuilder();

    @Override
    public LocalPlaylist compute(Set<User> users) {
        Set<User> unMutedUsers = new HashSet<>();
        for (User u : users) {
            if (!u.isMute()) {
                unMutedUsers.add(u);
            }
        }
        Map<String, Track> tracksList = new HashMap<>();
        Map<String, Map<String, Double>> scoreComputed =
                score.compute(unMutedUsers, tracksList);
        return computeLocalePlaylist(scoreComputed, unMutedUsers, tracksList);
    }

    public abstract LocalPlaylist computeLocalePlaylist(Map<String, Map<String,
            Double>> musicScorePerUser, Set<User> users, Map<String,
            Track> tracksList);
}
