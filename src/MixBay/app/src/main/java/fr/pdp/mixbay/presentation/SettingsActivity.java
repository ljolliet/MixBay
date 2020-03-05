package fr.pdp.mixbay.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.utils.UserSettingsAdapter;

public class SettingsActivity extends AppCompatActivity {

    private final int MUTE_POSITION = 0;
    private final int DELETE_POSITION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        ListView userListView = findViewById(R.id.userListView);

        setActionBar(toolbar);

        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        // Set onClick event on toolbar
        toolbar.setNavigationOnClickListener(view -> finish());

        // Add Users to ListView
        UserSettingsAdapter adapter = new UserSettingsAdapter(this, new ArrayList<>(Services.getSession().getUsers()));
        userListView.setAdapter(adapter);

        // Detect onClick on User
        userListView.setOnItemClickListener(this::onClickUser);
    }

    private void onClickUser(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView moreImageView = view.findViewById(R.id.userSettingsMore);
        PopupMenu popup = new PopupMenu(this, moreImageView);

        popup.getMenuInflater().inflate(R.menu.popup_user_settings_menu, popup.getMenu());


        // Get User
        User user = (User) adapterView.getItemAtPosition(i);

        // Change title of menu
        if (user.isMute())
            popup.getMenu().getItem(MUTE_POSITION).setTitle(R.string.unmute);

        if (Services.getCurrentUser().id.equals(user.id)) // TODO Replace with user.equals()
            popup.getMenu().getItem(DELETE_POSITION).setVisible(false);


        // Detect popup click event
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.mute:
                    toggleMuteUser(user, view);
                    break;
                case R.id.delete:
                    removeUser(user);
                    break;
                default:
                    Log.d("SettingsActivity", "Default: " + item.getItemId());
                    break;
            }

            return true;
        });

        popup.show();
    }

    private void toggleMuteUser(User user, View view) {
        TextView displayName = view.findViewById(R.id.userDisplayName);

        if (user.isMute()) {
            user.unmute();
            displayName.setTextColor(getResources().getColor(R.color.white, getTheme()));
        }
        else {
            user.mute();
            displayName.setTextColor(getResources().getColor(R.color.colorDisabled, getTheme()));
        }
    }

    private void removeUser(User user) {
        // Create popup to confirm
        // If OK -> remove
        // Else -> nothing happens
    }
}
