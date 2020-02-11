package fr.pdp.mixbay.models;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class FirstAlgo implements AlgoI {


    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute(Set<User> users) {

        Map<String,TrackFeatures> userProfile = new HashMap<>();
        Set<Track> tracksList = new HashSet<>();

        for (User u : users) {
            int numberTracks = 0;
            double danceabilityAv = 0.;
            double energyAv = 0.;
            double speechinessAv = 0.;
            double acousticnessAv = 0.;
            double instrumentalnessAv = 0.;
            for (Playlist p : u.getPlaylist())
                for (Track t : p.getTracks())
                {
                    tracksList.add(t);

                    numberTracks++;
                    danceabilityAv += t.features.danceability;
                    energyAv += t.features.energy;
                    speechinessAv += t.features.speechiness;
                    acousticnessAv += t.features.acousticness;
                    instrumentalnessAv += t.features.instrumentalness;
                }

            danceabilityAv /= numberTracks;
            energyAv /= numberTracks;
            speechinessAv /= numberTracks;
            acousticnessAv /= numberTracks;
            instrumentalnessAv /= numberTracks;

            TrackFeatures tF = new TrackFeatures(danceabilityAv, energyAv, speechinessAv, acousticnessAv, instrumentalnessAv);


            userProfile.put(u.id,tF);
        }

        Map<String,TrackFeatures> trackFeaturesDifferenceMap = new HashMap<>();

        Iterator getUser = userProfile.entrySet().iterator();
        while (getUser.hasNext()) {
            Map.Entry currentUser = (Map.Entry) getUser.next();

            Iterator getOtherUsers = userProfile.entrySet().iterator();
            while(getOtherUsers.hasNext()) {
                Map.Entry otherUser = (Map.Entry) getOtherUsers.next();
                if (otherUser.getKey() != currentUser.getKey()) {
                    double danceabilityUserDifference = ((TrackFeatures) currentUser.getValue()).danceability - ((TrackFeatures) otherUser.getValue()).danceability;
                    double energyUserDifference = ((TrackFeatures) currentUser.getValue()).energy - ((TrackFeatures) otherUser.getValue()).energy;
                    double speechinessUserDifference = ((TrackFeatures) currentUser.getValue()).speechiness - ((TrackFeatures) otherUser.getValue()).speechiness;
                    double acousticnessUserDifference = ((TrackFeatures) currentUser.getValue()).acousticness - ((TrackFeatures) otherUser.getValue()).acousticness;
                    double instrumentalnessUserDifference = ((TrackFeatures) currentUser.getValue()).instrumentalness - ((TrackFeatures) otherUser.getValue()).instrumentalness;
                    TrackFeatures trackFeaturesDifference = new TrackFeatures(danceabilityUserDifference,energyUserDifference,speechinessUserDifference,acousticnessUserDifference,instrumentalnessUserDifference);
                    trackFeaturesDifferenceMap.put(currentUser.getKey() + " - " + otherUser.getKey(), trackFeaturesDifference);
                }
            }
        }

        HashMap<String,HashMap<String,Double>> musicScorePerUser = new HashMap<>();
        getUser = userProfile.entrySet().iterator();
        while (getUser.hasNext()) {
            HashMap<String,Double> scorePerTrack = new HashMap<>();

            Map.Entry currentUser = (Map.Entry) getUser.next();

            Iterator tracksIt = tracksList.iterator();
            while(tracksIt.hasNext()) {
                Track t = (Track) tracksIt.next();
                Double score = (((TrackFeatures) currentUser.getValue()).danceability + ((TrackFeatures) currentUser.getValue()).energy + ((TrackFeatures) currentUser.getValue()).speechiness + ((TrackFeatures) currentUser.getValue()).acousticness + ((TrackFeatures) currentUser.getValue()).instrumentalness) -
                               (t.features.danceability + t.features.energy + t.features.speechiness + t.features.acousticness + t.features.instrumentalness);
                scorePerTrack.put(t.id, score);
            }
            musicScorePerUser.put((String) currentUser.getKey(), scorePerTrack);
        }

        Map<String,Double> leastMiseryComputed = new HashMap<>();

        return null;
    }

}
