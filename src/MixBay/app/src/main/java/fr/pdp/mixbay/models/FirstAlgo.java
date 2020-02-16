package fr.pdp.mixbay.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FirstAlgo implements AlgoI {

    private Map<String, TrackFeatures> userProfile = new HashMap<>();
    private Map<String, Track> tracksList = new HashMap<>();
    private Set<User> users;
    private HashMap<String, HashMap<String, Double>> musicScorePerUser = new HashMap<>();
    private Map<String, Double> leastMiseryComputed = new LinkedHashMap<>();

    private int numberTracks = 0;
    private double[] trackFeaturesAverage = new double[TrackFeatures.SIZE];

    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute(Set<User> users) {

        this.users = users;
        for (User u : users) {
            for (int i = 0; i < trackFeaturesAverage.length; i++) {
                trackFeaturesAverage[i] = 0.;
            }
            computeUserProfile(u);
        }
        computeMusicScorePerUser();
        return computeLocalePlaylist();
    }

    /**
     * Compute an "user profile" for the given user. It is the average of the tracks features of each tracks from this user's playlists.
     *
     * @param u Specific user of the application.
     */
    public void computeUserProfile(User u) {

        for (Playlist p : u.getPlaylist()) {
            for (Track t : p.getTracks()) {
                tracksList.put(t.id, t);
                numberTracks++;
                for (int i = 0; i < trackFeaturesAverage.length; i++) {
                    trackFeaturesAverage[i] = ((HashMap<TrackFeatures.Name, Double>) t.features.getAll()).get(TrackFeatures.Name.values()[i]);
                }
            }
        }

        for (int i = 0; i < trackFeaturesAverage.length; i++) {
            trackFeaturesAverage[i] /= numberTracks;
        }
        TrackFeatures tF = new TrackFeatures(trackFeaturesAverage);
        userProfile.put(u.id, tF);
    }

    /**
     * Assign a score to each track for each user
     */
    public void computeMusicScorePerUser() {
        Iterator getUser = userProfile.entrySet().iterator();
        while (getUser.hasNext()) {
            HashMap<String, Double> scorePerTrack = new HashMap<>();
            Map.Entry currentUser = (Map.Entry) getUser.next();
            Iterator tracksIt = tracksList.entrySet().iterator();
            while (tracksIt.hasNext()) {
                Map.Entry trackEntry = (Map.Entry) tracksIt.next();
                Track t = (Track) trackEntry.getValue();
                double currentUserScore = 0.;
                double currentTrackScore = 0.;
                for (int i = 0; i < trackFeaturesAverage.length; i++) {
                    currentUserScore += ((TrackFeatures) currentUser.getValue()).getAll().get(TrackFeatures.Name.values()[i]);
                    currentTrackScore += t.features.getAll().get(TrackFeatures.Name.values()[i]);
                }
                currentUserScore /= TrackFeatures.SIZE;
                currentTrackScore /= TrackFeatures.SIZE;
                scorePerTrack.put(t.id, Math.abs(currentUserScore - currentTrackScore));
            }
            musicScorePerUser.put((String) currentUser.getKey(), scorePerTrack);
        }
    }

    /**
     * Compute a local playlist of Playlist.SIZE_MAX (25) tracks.
     * The 25 tracks with the lowest computed score are chosen to be part of the locale playlist (Least Misery Strategy).
     *
     * @return A playlist of 25 tracks.
     */
    public Playlist computeLocalePlaylist() {
        Iterator tracksIt = tracksList.entrySet().iterator();
        while (tracksIt.hasNext()) {
            Map.Entry trackEntry = (Map.Entry) tracksIt.next();
            Track t = (Track) trackEntry.getValue();
            Double leastScore = 100.;
            for (User u : users) {
                if ((musicScorePerUser.get(u.id)).get(t.id) < leastScore) {
                    leastScore = (musicScorePerUser.get(u.id)).get(t.id);
                }
            }
            leastMiseryComputed.put(t.id, leastScore);
        }
        leastMiseryComputed = MapUtil.sortByValue(leastMiseryComputed);
        Iterator computedTracksIterator = leastMiseryComputed.entrySet().iterator();
        int index = 0;
        Playlist p = new Playlist("001", "locale playlist");
        while (computedTracksIterator.hasNext() && index < Playlist.SIZE_MAX) {
            Map.Entry entryTrack = (Map.Entry) computedTracksIterator.next();
            p.addTrack(tracksList.get(entryTrack.getKey()));
            index++;
        }
        return p;
    }
}
