package fr.pdp.mixbay.business.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.Track;

//TODO comments

public class TrackAdapter extends BaseAdapter {

    private List<Track> trackList;
    private LayoutInflater inflater;

    public TrackAdapter(Context context, List<Track> trackList) {
        this.trackList = trackList;
        this.inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.list_track_item, parent, false);

        TrackViewHolder viewHolder = (TrackViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TrackViewHolder();
            viewHolder.title = convertView.findViewById(R.id.trackTitle);
            viewHolder.artist = convertView.findViewById(R.id.artistName);
            viewHolder.userInitial = convertView.findViewById(R.id.userInitial);
            convertView.setTag(viewHolder);
        }

        Track track = (Track) getItem(position);

        viewHolder.title.setText(track.getTitle());
        viewHolder.artist.setText(makeArtistsString(track));
//        viewHolder.userInitial.setText(track.getUser().getInitial());

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
        Button userInitial;
    }
}
