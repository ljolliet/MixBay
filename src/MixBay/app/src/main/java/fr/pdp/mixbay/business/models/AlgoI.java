/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import android.content.Context;

import java.util.Set;

public interface AlgoI {

    /**
     * @param context The context to access to the resources of the application
     * @return The Algorithm name
     */
    String getName(Context context);

    /**
     * Compute the mixing algorithm
     *
     * @param users A set of users with data loaded from a streaming API
     * @return A playlist that mixes all users's playlists
     */
    LocalPlaylist compute(Set<User> users);
}
