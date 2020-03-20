package fr.pdp.mixbay.business.algorithms;

import android.content.Context;

import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.AlgoI;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;

public class RandomAlgo implements AlgoI {


    @Override
    public String getName(Context context) {
//        return this.getClass().toString() + " with " + score.getClass().toString();
        return context.getString(R.string.algo_random);
    }

    @Override
    public LocalPlaylist compute(Set<User> users) {
        int nbUser = 0;

        for(User u : users)
            if (!u.isMute())
                nbUser++;

        int tracksPerUser = LocalPlaylist.SIZE_MAX / nbUser; // cast
        LocalPlaylist newPlaylist = new LocalPlaylist();

        for(User u : users)
            if (!u.isMute())
                for(Playlist p : u.getAllPlaylists()) {
                    for (int i = 0; i < tracksPerUser; i++)
                        newPlaylist.addTrack((Track) p.getTracks().toArray()[i]);
                    break;
                }
        return newPlaylist;
    }

}