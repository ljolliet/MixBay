package fr.pdp.mixbay.business.algorithms;

import java.util.Set;

import fr.pdp.mixbay.business.models.AlgoI;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;

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

        int tracksPerUser = Playlist.SIZE_MAX / nbUser; // cast
        Playlist newPlaylist = new Playlist("001", "locale playlist");

        for(User u : users)
            if (!u.isMute())
                for(Playlist p : u.getPlaylist()) {
                    for (int i = 0; i < tracksPerUser; i++)
                        newPlaylist.addTrack((Track) p.getTracks().toArray()[i]);
                    break;
                }
        return newPlaylist;
    }

}