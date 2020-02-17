package fr.pdp.mixbay.application;

import fr.pdp.mixbay.models.Playlist;
import fr.pdp.mixbay.models.Session;
import fr.pdp.mixbay.models.Track;
import fr.pdp.mixbay.models.TrackFeatures;
import fr.pdp.mixbay.models.User;

public class Services {

    @Deprecated
    public static void randomInit(Session session){
        User firstUser = new User("123", "tmp");
        User secondUser = new User("456","tmp2");
        User thirdUser = new User("789", "tmp3");
        session.setMainUser(firstUser);
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
}
