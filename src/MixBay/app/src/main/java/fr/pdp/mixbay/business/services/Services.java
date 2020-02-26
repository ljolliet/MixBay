package fr.pdp.mixbay.business.services;

import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.dataAccess.RepositoryI;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Session;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.TrackFeatures;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.data.InMemoryRepository;
import fr.pdp.mixbay.data.SpotifyAPI;

public class Services {

    private static RepositoryI repository = new InMemoryRepository();

    private Services() {} // Private constructor

    @Deprecated
    public static void randomInit(Session session){
        User firstUser = new User("123", "tmp");
        User secondUser = new User("456","tmp2");
        User thirdUser = new User("789", "tmp3");
        session.setCurrentUser(firstUser);
        session.addUser(secondUser);
        session.addUser(thirdUser);
        int playlistID = 0;
        int trackID = 0;

        for(User u : session.getUsers())
        {
            Playlist p = new Playlist(Integer.toString(++playlistID), "Playlist "+ playlistID);
            for(int i=0; i< 100; i++){
                TrackFeatures features = new TrackFeatures(Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random());
                p.addTrack(new Track(Integer.toString(++trackID), "Track " + trackID, "Random Album", "Random Artist", "Random Cover", features));
            }
            u.addPlaylist(p);
        }
        System.out.println(session.getUsers());
    }

    public static Session getSession() {
        return repository.getSession();
    }

    public static void setSession(Session session) {
        repository.setSession(session);
    }

    public static Session createSpotifySession() {
        APIManagerI api = new SpotifyAPI();
        Session session = new Session(api);

        setSession(session);

        return session;
    }

    public static boolean disconnectSession() {
        return repository.getSession().end();
    }

    public static void previousMusic() {
        repository.getSession().previousMusic();
    }

    public static void playMusic() {
        repository.getSession().playMusic();
    }

    public static void nextMusic() {
        repository.getSession().nextMusic();
    }
}
