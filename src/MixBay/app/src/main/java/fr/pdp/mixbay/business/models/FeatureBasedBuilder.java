package fr.pdp.mixbay.business.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FeatureBasedBuilder implements ScoreBuilderI {


    private Map<String, Track> tracksList = new HashMap<>();
    private double[] trackFeaturesAverage = new double[TrackFeatures.SIZE];


    @Override
    public String getName() {
        return this.getClass().toString();
    }

    /**
     * Compute an "user profile" for the given user. It is the average of the tracks features of each tracks from this user's playlists
     *
     * @param u Specific user of the application
     */
    public TrackFeatures computeUserProfile(User u, Map<String, Track> tracksList) {
        for (Playlist p : u.getPlaylist()) {
            for (Track t : p.getTracks()) {
                tracksList.put(t.id, t);
                for (int i = 0; i < trackFeaturesAverage.length; i++) {
                    trackFeaturesAverage[i] = t.getFeatures().allFeatures.get(TrackFeatures.NAME_VALUES[i]);
                }
            }
        }

        for (int i = 0; i < trackFeaturesAverage.length; i++) {
            trackFeaturesAverage[i] /= tracksList.size();
        }
        TrackFeatures tF = new TrackFeatures(trackFeaturesAverage);
        this.tracksList = tracksList;
        return tF;
    }


    /**
     * Assign a score to each track for each user
     *
     * @param userProfile A Map containing a computed profile for each user. The keys are the user's ID and the values are TrackFeatures objects
     * @return A structure containing the computed personnal score of each track for each user
     */

    public HashMap<String, Map<String, Double>> computeMusicScorePerUser(Map<String, TrackFeatures> userProfile) {
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
                    scorePerTrackVector[i] = ((TrackFeatures) currentUser.getValue()).allFeatures.get(TrackFeatures.NAME_VALUES[i]) - t.getFeatures().allFeatures.get(TrackFeatures.NAME_VALUES[i]);
                    currentTrackScore += Math.pow(scorePerTrackVector[i],2);
                }
                currentTrackScore = Math.sqrt(currentTrackScore);
                scorePerTrack.put(t.id, currentTrackScore);
            }
            musicScorePerUser.put((String) currentUser.getKey(), scorePerTrack);
        }
        return musicScorePerUser;
    }


    @Override
    public Map<String, Map<String, Double>> compute(Set<User> users, Map<String, Track> tracksList) {
        Map<String, TrackFeatures> userProfile = new HashMap<>();
        for (User u : users) {
            for (int i = 0; i < trackFeaturesAverage.length; i++) {
                trackFeaturesAverage[i] = 0.;
            }
            userProfile.put(u.id,computeUserProfile(u, tracksList));
        }

        return computeMusicScorePerUser(userProfile);
    }
}
