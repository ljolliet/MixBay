package fr.pdp.mixbay.models;

import java.util.Set;

public class RandomAlgo implements AlgoI {

    Set<User> users;

    public RandomAlgo(Set<User> users){
        setUsers(users);
    }

    @Override
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute() {
        Playlist newPlaylist = new Playlist("001", "locale playlist");
        for(User u : users) {
            if (!u.isMute()) {
                for(Playlist p : u.getPlaylist()) {
                    for(int i = 0; i<12; i++)
                        newPlaylist.addTrack((Track) p.getTracks().toArray()[i]);
                }
            }
        }
        return null;
    }

}