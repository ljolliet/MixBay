package fr.pdp.mixbay.business.models;


import java.util.HashMap;
import java.util.Map;

public class TrackFeatures {

    public enum Name{
        DANCEABILITY,
        ENERGY,
        SPEECHINESS,
        ACOUSTICNESS,
        INSTRUMENTALNESS,
        LIVENESS,
        VALENCE
    }

    public static final Name[] NAME_VALUES = Name.values();
    public static final int SIZE = NAME_VALUES.length;

    public final Map<Name, Double> allFeatures;

    /**
     *   A TrackFeature is a list of features representing a Track. There are all values from 0.0 to 1.0.
     *   This analysis is provided by the Spotify API.
     *   The followings features documentation is based on Spotify Audio Feature Documentation.
     *
     * @param danceability "Describes how suitable a track is for dancing based on a combination of musical elements including tempo, rhythm stability, beat strength, and overall regularity."
     * @param energy "A measure that represents a perceptual measure of intensity and activity. Typically, energetic tracks feel fast, loud, and noisy."
     * @param speechiness "Detects the presence of spoken words in a track."
     * @param acousticness "A confidence measure of whether the track is acoustic."
     * @param instrumentalness "Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”."
     * @param liveness "Detects the presence of an audience in the recording. Higher liveness values represent an increased probability that the track was performed live."
     * @param valence "Describing the musical positiveness conveyed by a track. Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence sound more negative (e.g. sad, depressed, angry)."
     */
    public TrackFeatures(double danceability, double energy, double speechiness, double acousticness, double instrumentalness, double liveness, double valence) {
        this.allFeatures = new HashMap<Name, Double>(){{
            put(Name.DANCEABILITY, danceability);
            put(Name.ENERGY, energy);
            put(Name.SPEECHINESS, speechiness);
            put(Name.ACOUSTICNESS, acousticness);
            put(Name.INSTRUMENTALNESS, instrumentalness);
            put(Name.LIVENESS, liveness);
            put(Name.VALENCE, valence);
        }};
    }

    /**
     *   A TrackFeature is a list of features representing a Track. There are all values from 0.0 to 1.0.
     *   This analysis is provided by the Spotify API.
     * @param trackFeatures An array containing values from 0.0 to 1.0 corresponding to (in the following order :
     *                      danceability, energy, speechiness, acousticness, instrumentalness, liveness, valence
     */
    public TrackFeatures(double[] trackFeatures) {
        this.allFeatures = new HashMap<Name, Double>(){{
            for(int i = 0; i< SIZE; i++)
                put(NAME_VALUES[i], trackFeatures[i]);
        }};
    }
}