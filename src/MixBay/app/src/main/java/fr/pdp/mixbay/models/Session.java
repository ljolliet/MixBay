package fr.pdp.mixbay.models;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;


public class Session {

    private Playlist localePlaylist;
    private User mainUser;
    private User currentuser;
    private Set<User> users;
    private AlgoI algo;
    private LogManagerI logManager;
    private APIManagerI apiManager;

    public Session(APIManagerI api) {
        this.apiManager = api;
        this.localePlaylist = new Playlist("01", "MixBay Playlist");
        this.users = new HashSet<>();
        this.algo = new RandomAlgo();

    }

    public void start(Context context) {
        apiManager.connect(context);
        randomInit();
        mix();
    }

    public void mix(){
        localePlaylist = algo.compute(this.users);
    }

    @Deprecated
    private void randomInit(){
        mainUser = new User("123", "tmp");
        User secondUser = new User("456","tmp2");
        currentuser = mainUser;
        users.add(mainUser);
        users.add(secondUser);
        int playlistID = 0;
        int trackID = 0;

        for(User u : users)
        {
            Playlist p = new Playlist(Integer.toString(++playlistID), "Playlist "+ playlistID);
            for(int i=0; i< 100; i++){
                TrackFeatures features = new TrackFeatures(Math.random(), Math.random(), Math.random(), Math.random(), Math.random());
                p.addTrack(new Track(Integer.toString(++trackID), "Track " + trackID, "Random Album", "Random Artist", "Random Cover", features));
            }
            u.addPlaylist(p);
        }
        System.out.println(users);
    }

    public void end() {
        apiManager.disconnect();
    }
}
