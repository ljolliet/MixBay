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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.ScoreBuilderI;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.TrackFeatures;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.utils.MapUtil;

public class FeatureBasedBuilder implements ScoreBuilderI {


    public static final double RANDOM_RATIO = 0.15;
    private static final double MAX_RANDOM = RANDOM_RATIO;
    private static final double MIN_RANDOM = -MAX_RANDOM;

    private Map<String, Track> tracksList = new HashMap<>();
    private double[] trackFeaturesAverage = new double[TrackFeatures.SIZE];


    @Override
    public String getName() {
        return "a score builder based on Spotify Track Features";
    }

    /**
     * Compute an "user profile" for the given user. It is the average of the
     * tracks features of each tracks from this user's playlists
     *
     * @param u Specific user of the application
     */
    public TrackFeatures computeUserProfile(User u, Map<String,
            Track> tracksList) {
        for (Playlist p : u.getAllPlaylists()) {
            for (Track t : p.getTracks()) {
                tracksList.put(t.id, t);
                for (int i = 0; i < trackFeaturesAverage.length; i++) {
                    double feature = t.getFeature(TrackFeatures.NAME_VALUES[i]);
                    if(feature != -1.)
                        trackFeaturesAverage[i] += feature;
                }
            }
        }

        for (int i = 0; i < trackFeaturesAverage.length; i++) {
            trackFeaturesAverage[i] /= tracksList.size();
        }
        TrackFeatures tF = new TrackFeatures(trackFeaturesAverage);
        Iterator tracksListIt = tracksList.entrySet().iterator();
        while (tracksListIt.hasNext()) {
            Map.Entry trackEntry = (Map.Entry) tracksListIt.next();
            this.tracksList.put((String) trackEntry.getKey(), (Track) trackEntry.getValue());
        }
        return tF;
    }


    /**
     * Assign a score to each track for each user
     *
     * @param userProfile A Map containing a computed profile for each user.
     *                    The keys are the user's ID and the values
     *                    are TrackFeatures objects
     * @return A structure containing the computed personnal score of each track
     * for each user
     */

    public HashMap<String, Map<String, Double>>
    computeMusicScorePerUser(Map<String, TrackFeatures> userProfile) {
        HashMap<String, Map<String, Double>> musicScorePerUser = new HashMap<>();
        double[] scorePerTrackVector = new double[TrackFeatures.SIZE];

        Iterator getUser = userProfile.entrySet().iterator();
        while (getUser.hasNext()) {
            HashMap<String, Double> scorePerTrack = new HashMap<>();
            Map.Entry currentUser = (Map.Entry) getUser.next();
            Iterator tracksIt = tracksList.entrySet().iterator();
            while (tracksIt.hasNext()) {
                Map.Entry trackEntry = (Map.Entry) tracksIt.next();
                Track t = (Track) trackEntry.getValue();
                double currentTrackScore = 0.;
                for (int i = 0; i < trackFeaturesAverage.length; i++) {
                    double userFeature =
                            ((TrackFeatures) currentUser.getValue())
                                    .allFeatures.get(TrackFeatures.NAME_VALUES[i]);
                    double trackFeature =
                            t.getFeature(TrackFeatures.NAME_VALUES[i]);
                    double randomShift =
                            MIN_RANDOM + (Math.random()*(MAX_RANDOM- MIN_RANDOM));
                    if (trackFeature != -1.)
                        scorePerTrackVector[i] =
                                userFeature - ( trackFeature + randomShift);
                    currentTrackScore += Math.pow(scorePerTrackVector[i], 2);
                }
                currentTrackScore = Math.sqrt(currentTrackScore);
                scorePerTrack.put(t.id, currentTrackScore);
            }
            musicScorePerUser.put((String) currentUser.getKey(),
                    MapUtil.sortByValue(scorePerTrack));
        }
        return musicScorePerUser;
    }


    @Override
    public Map<String, Map<String, Double>> compute(Set<User> users,
                                                    Map<String,
                                                            Track> tracksList) {
        Map<String, TrackFeatures> userProfile = new HashMap<>();
        for (User u : users) {
            userProfile.put(u.id, computeUserProfile(u, tracksList));
        }

        return computeMusicScorePerUser(userProfile);
    }
}
