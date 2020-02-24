package fr.pdp.mixbay.business.models;

import java.util.Map;
import java.util.Set;

public interface ScoreBuilderI {

    /**
     * @return The score builder name
     */
    String getName();

    /**
     * TODO
     *
     * @param users A set of users with datas loaded from a streaming API
     * @return A structure containing the computed personnal score of each track for each user
     */
    Map<String, Map<String,Double>> compute(Set<User> users, Map<String, Track> tracksList);
}
