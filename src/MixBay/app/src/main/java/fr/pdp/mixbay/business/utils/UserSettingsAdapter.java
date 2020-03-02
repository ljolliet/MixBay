package fr.pdp.mixbay.business.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.User;

public class UserSettingsAdapter extends BaseAdapter {

    List<User> userList;
    private LayoutInflater inflater;

    public UserSettingsAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_user_settings_item, parent, false);

        User user = (User) getItem(position);

        TextView displayName = convertView.findViewById(R.id.userDisplayName);

        displayName.setText(user.username);

        return convertView;
    }
}