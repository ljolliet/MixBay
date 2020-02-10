package fr.pdp.mixbay.models;


//TODO think about a different architecture

class TrackFeatures {

    public final double danceability;
    public final double energy;
    public final double speechiness;
    public final double acousticness;
    public final double instrumentalness;


    TrackFeatures(double danceability, double energy, double speechiness, double acousticness, double instrumentalness) {
        this.danceability = danceability;
        this.energy = energy;
        this.speechiness = speechiness;
        this.acousticness = acousticness;
        this.instrumentalness = instrumentalness;
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