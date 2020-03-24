/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.business.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;

public class TrackAdapter extends BaseAdapter {

    private List<Track> trackList;
    private LayoutInflater inflater;
    private Context context;

    public TrackAdapter(Context context, List<Track> trackList) {
        this.trackList = trackList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_track_item, parent,
                    false);

        Track track = (Track) getItem(position);
        List<User> userList = new ArrayList<>(Factory.getUsersForTrack(track));

        TrackViewHolder viewHolder = (TrackViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new TrackViewHolder();
            viewHolder.title = convertView.findViewById(R.id.trackTitle);
            viewHolder.artist = convertView.findViewById(R.id.artistName);
            viewHolder.initialTextView = convertView
                    .findViewById(R.id.userInitial);
            viewHolder.otherUsersTextView = convertView
                    .findViewById(R.id.otherUsers);

            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(track.getTitle());
        viewHolder.artist.setText(makeArtistsString(track));
        // Set initial of 1 user
        viewHolder.initialTextView.setText(
                String.valueOf(userList.get(0).initial));
        // Set color of the View
        Drawable background = viewHolder.initialTextView.getBackground();
        ((GradientDrawable) background).setColor(userList.get(0).getColor());

        // Display TextView if they are other users
        if (userList.size() > 1) {
            viewHolder.otherUsersTextView.setVisibility(View.VISIBLE);
            String size = "+" + (userList.size() - 1);
            viewHolder.otherUsersTextView.setText(size);

            // Set color of the View
            Drawable background1 = viewHolder.otherUsersTextView.getBackground();
            ((GradientDrawable) background1).setColor(context.getResources()
                    .getColor(R.color.grey, context.getTheme()));
        }
        // Else, set visibility to false
        else
            viewHolder.otherUsersTextView.setVisibility(View.GONE);

        return convertView;
    }

    private String makeArtistsString (Track track) {
        Set artistList = track.getArtists();
        StringBuilder string = new StringBuilder();

        Iterator it = artistList.iterator();

        while (it.hasNext()) {
            string.append(it.next());

            if (it.hasNext())
                string.append(", ");
        }
        return string.toString();
    }

    private class TrackViewHolder {
        TextView title;
        TextView artist;
        TextView initialTextView;
        TextView otherUsersTextView;
    }
}
