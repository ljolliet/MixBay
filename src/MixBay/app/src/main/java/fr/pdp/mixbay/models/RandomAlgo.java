package fr.pdp.mixbay.models;

import java.util.Set;

public class RandomAlgo implements AlgoI {


    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute(Set<User> users) {
        int nbUser = 0;
        for(User u : users)
            if (!u.isMute())
                nbUser++;
        int tracksPerUser = (int) Playlist.SIZE_MAX / nbUser;

            Playlist newPlaylist = new Playlist("001", "locale playlist");

        for(User u : users) {
            if (!u.isMute()) {
                for(Playlist p : u.getPlaylist()) {
                    for(int i = 0; i<tracksPerUser; i++)
                        newPlaylist.addTrack((Track) p.getTracks().toArray()[i]);
                    break;
                }
            }
        }
        return newPlaylist;
    }

}