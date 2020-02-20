package fr.pdp.mixbay.business.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
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

        LinearLayout layoutItem;

        if(convertView == null) {
            layoutItem = (LinearLayout) inflater.inflate(R.layout.list_track_item, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView title = layoutItem.findViewById(R.id.trackTitle);
        TextView artistName = layoutItem.findViewById(R.id.artistName);

        artistName.setText(makeArtistsString(trackList.get(position)));
        title.setText(trackList.get(position).getTitle());

        return layoutItem;
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
}
