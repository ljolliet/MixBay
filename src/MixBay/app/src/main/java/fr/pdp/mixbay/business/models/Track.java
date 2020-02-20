package fr.pdp.mixbay.business.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Track {

        public final String id;
        public final String title;
        public final String album;
        public final Set<String> artists;
        public final String cover_url;
        private TrackFeatures features;


    /**
     * A track is an unchangeable public object. It is based on music streaming API datas.
     * @param id unique id of the track.
     * @param title track's title.
     * @param album track's album.
     * @param artists track's author(s).
     * @param cover_url cover's url of the track's album.
     */
    public Track(String id, String title, String album, Set<String> artists, String cover_url) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artists = artists;
        this.cover_url = cover_url;
    }

    /**
     * A track is an unchangeable public object. It is based on music streaming API datas.
     * @param id unique id of the track.
     * @param title track's title.
     * @param album track's album.
     * @param artists track's author(s).
     * @param cover_url cover's url of the track's album.
     * @param features TODO
     */
    @Deprecated
    public Track(String id, String title, String album, Set<String> artists, String cover_url, TrackFeatures features) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artists = artists;
        this.cover_url = cover_url;
        this.features = features;
    }

    /**
     * A track is an unchangeable public object. It is based on music streaming API datas.
     * To simplify developers job, this constructor takes a unique artist and calls the previous constructor.
     * @param id unique id of the track.
     * @param title track's title.
     * @param album track's album.
     * @param artist track's author.
     * @param cover_url cover's url of the track's album.
     * @param features TODO
     */
    @Deprecated
    public Track(String id, String title, String album, String artist, String cover_url, TrackFeatures features) {
        this(id,title,album, new HashSet<>(Collections.singletonList( artist )), cover_url, features);
    }

    public TrackFeatures getFeatures() {
        return features;
    }

    public void setFeatures(TrackFeatures features) {
        this.features = features;
    }

    /**
     * Equal based on track's id.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(o instanceof Track) {
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

    public String getTitle() {
        return title;
    }

    public Set<String> getArtists() {
        return new HashSet<>(this.artists);
    }

    /*
     * this function is just for testing the front (printing the track list).
     * It could be deleted after that
     */

    public static List<Track> getSampleTracks() {
        List<Track> trackList = new ArrayList<Track>();
        Set<String> artistsSet = new HashSet<String>();
        artistsSet.add("Dr Dre");
        artistsSet.add("2pac");

        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));
        trackList.add(new Track("id", "kill him", "LifeStyle", artistsSet, "url", new TrackFeatures(0, 0, 0, 0, 0,0, 0)));

        return trackList;
    }
}
