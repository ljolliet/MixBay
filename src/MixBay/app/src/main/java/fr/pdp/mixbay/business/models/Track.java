/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Track {

    public final String id;
    public final String title;
    public final String album;
    public final Set<String> artists;
    public final String cover_url;
    private TrackFeatures features;
    private boolean liked;


    /**
     * A track is an unchangeable public object. It is based on music streaming
     * API datas.
     *
     * @param id        Unique id of the track.
     * @param title     Track's title.
     * @param album     Track's album.
     * @param artists   Track's author(s).
     * @param cover_url Cover's url of the track's album.
     */
    public Track(String id, String title, String album, Set<String> artists,
                 String cover_url) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artists = artists;
        this.cover_url = cover_url;
        this.liked = false;
    }

    public TrackFeatures getFeatures() {
        return features;
    }

    public void setFeatures(TrackFeatures features) {
        this.features = features;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getArtists() {
        return new HashSet<>(this.artists);
    }

    /**
     * Equal based on track's id.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Track) {
            Track track = (Track) o;
            return this.id.equals(track.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public boolean isLiked() {
        return liked;
    }

    public void like() {
        this.liked = true;
    }

    public void unlike() {
        this.liked = false;
    }

    /**
     * Get a particular feature
     * @param name The name of the feature to get
     * @return the dfeature value if there is feature, -1 otherwise
     */
    public double getFeature(TrackFeatures.Name name) {
        if(this.features != null && name != null)
            return this.features.allFeatures.get(name);
        return -1.;
    }
}
