package fr.pdp.mixbay.business.models;

import java.util.Set;

public interface AlgoI {

    /**
     * @return The Algorithm name
     */
    String getName();

    /**
     * Compute the mixing algorithm
     * @param users A set of users with data loaded from a streaming API
     * @return A playlist that mixes all users's playlists
     * */
    LocalPlaylist compute(Set<User> users);
}
