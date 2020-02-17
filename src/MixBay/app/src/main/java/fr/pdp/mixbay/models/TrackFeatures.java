package fr.pdp.mixbay.models;


//TODO think about a different architecture

import java.util.HashMap;
import java.util.Map;

public class TrackFeatures {

    public static enum Name{
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

    @Deprecated
    public TrackFeatures(double danceability, double energy, double speechiness, double acousticness, double instrumentalness) {
        this.allFeatures = new HashMap<Name, Double>(){{
            put(Name.DANCEABILITY, danceability);
            put(Name.ENERGY, energy);
            put(Name.SPEECHINESS, speechiness);
            put(Name.ACOUSTICNESS, acousticness);
            put(Name.INSTRUMENTALNESS, instrumentalness);
        }};
    }

    public TrackFeatures(double[] trackFeatures) {
        this.allFeatures = new HashMap<Name, Double>(){{
            for(int i = 0; i< SIZE; i++)
                put(NAME_VALUES[i], trackFeatures[i]);
        }};
    }

    @Deprecated
    public Map<Name, Double> getAll() {
        return this.allFeatures;
    }

}

/*
Typical request :
      "danceability": 0.735,
      "energy": 0.578,
      "key": 5,
      "loudness": -11.84,
      "mode": 0,
      "speechiness": 0.0461,
      "acousticness": 0.514,
      "instrumentalness": 0.0902,
      "liveness": 0.159,
      "valence": 0.636,
      "tempo": 98.002,
      "type": "audio_features",
      "id": "06AKEBrKUckW0KREUWRnvT",
      "uri": "spotify:track:06AKEBrKUckW0KREUWRnvT",
      "track_href": "https://api.spotify.com/v1/tracks/06AKEBrKUckW0KREUWRnvT",
      "analysis_url": "https://api.spotify.com/v1/audio-analysis/06AKEBrKUckW0KREUWRnvT",
      "duration_ms": 255349,
      "time_signature": 4
 */