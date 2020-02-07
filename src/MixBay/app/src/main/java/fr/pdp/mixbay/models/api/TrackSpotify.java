package fr.pdp.mixbay.models.api;

import com.spotify.protocol.types.Album;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;

import java.util.List;

public class TrackSpotify extends Track {

    public TrackSpotify(Artist artist, List<Artist> artists, Album album, long duration, String name, String uri, ImageUri imageUri, boolean isEpisode, boolean isPodcast) {
        super(artist, artists, album, duration, name, uri, imageUri, isEpisode, isPodcast);
    }
}
