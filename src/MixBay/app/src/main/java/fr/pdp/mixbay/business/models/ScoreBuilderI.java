/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import java.util.Map;
import java.util.Set;

public interface ScoreBuilderI {

    /**
     * @return The score builder name
     */
    String getName();

    /**
     * Compute the score builder algorithme in order to get a score per track
     * pet user.
     *
     * @param users A set of users with datas loaded from a streaming API
     * @return A structure containing the computed personnal score of each
     * track for each user
     */
    Map<String, Map<String, Double>> compute(Set<User> users, Map<String,
            Track> tracksList);
}
