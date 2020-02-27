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
    private Context context;
    private LayoutInflater inflater;

    public TrackAdapter(Context context, List<Track> trackList) {
        this.context = context;
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.trackTitle);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artistName);
            viewHolder.userInitial = (Button) convertView.findViewById(R.id.userInitial);
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
        String string = new String();
        Iterator it = artistList.iterator();
        while (it.hasNext()) {
            string += it.next();
            if (it.hasNext())
                string += " feat ";
        }
        return string;
    }

    private class TrackViewHolder {
        public TextView title;
        public TextView artist;
        public Button userInitial;
    }
}
