package fr.pdp.mixbay.models;

import java.util.Set;

public interface AlgoI {

    /**
     * @return The Algorithm name.
     */
    String getName();

    /**
     * Compute the mixing algorithm.
     * @param users A set of users with datas loaded from a streaming API.
     * @return A playlist that mixes all users's playlists.
     * */
    Playlist compute(Set<User> users);
}
