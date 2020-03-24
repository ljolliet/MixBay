/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.presentation;

import java.util.Objects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.algorithms.Fairness;
import fr.pdp.mixbay.business.algorithms.LeastMisery;
import fr.pdp.mixbay.business.algorithms.MostPleasure;
import fr.pdp.mixbay.business.algorithms.RandomAlgo;
import fr.pdp.mixbay.business.models.AlgoI;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.utils.UserSettingsAdapter;

public class SettingsActivity extends AppCompatActivity {

    private final int MUTE_POSITION = 0;
    private final int DELETE_POSITION = 1;

    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        userListView = findViewById(R.id.userListView);

        setActionBar(toolbar);

        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        // Set onClick event on toolbar
        toolbar.setNavigationOnClickListener(view -> finish());

        // Add Users to ListView
        this.loadUserList();

        // Detect onClick on User
        userListView.setOnItemClickListener(this::onClickUser);

        // Init algorithm View
        initAlgorithmDisplay();
    }

    private void initAlgorithmDisplay() {
        TextView algorithmTextView = findViewById(R.id.chosenAlgorithmTextView);
        algorithmTextView.setText(Services.getAlgorithmName(this));
    }

    private void loadUserList() {
        UserSettingsAdapter adapter = new UserSettingsAdapter(this,
                Services.getUsers());
        userListView.setAdapter(adapter);
    }

    private void onClickUser(AdapterView<?> adapterView, View view, int i,
                             long l) {
        ImageView moreImageView = view.findViewById(R.id.userSettingsMore);
        PopupMenu popup = new PopupMenu(this, moreImageView);

        popup.getMenuInflater().inflate(R.menu.popup_user_settings_menu,
                popup.getMenu());


        // Get User
        User user = (User) adapterView.getItemAtPosition(i);

        // Change title of menu
        if (user.isMute())
            popup.getMenu().getItem(MUTE_POSITION).setTitle(R.string.unmute);

        if (Services.isCurrentUser(user))
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
                    Log.d("SettingsActivity", "Default: "
                            + item.getItemId());
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
            displayName.setTextColor(getResources().getColor(R.color.white,
                    getTheme()));
        }
        else {
            user.mute();
            displayName.setTextColor(getResources()
                    .getColor(R.color.colorDisabled, getTheme()));
        }
    }

    private void removeUser(User user) {
        // Create a confirm popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.AlertDialogStyle);
        builder.setTitle(getString(R.string.remove_user_alert_title,
                user.username));
        builder.setMessage(R.string.remove_user_message);

        // OK button
        builder.setPositiveButton(R.string.OK, (dialog, which) -> {
            // If OK, remove the user
            Services.removeUser(user);
            // Reload userList
            loadUserList();
        });

        // Cancel button
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void onClickDisconnect(View view){
        Services.disconnectSession();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        onStop();
    }

    public void onClickSelectAlgorithm(View view) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(R.string.choose_algo_alert_title);

        builder.setItems(R.array.algorithms, (dialogInterface, i) -> {
            AlgoI algorithm;
            switch (i) {
                case 1:
                    algorithm = new Fairness();
                    break;
                case 2:
                    algorithm = new MostPleasure();
                    break;
                case 3:
                    algorithm = new RandomAlgo();
                    break;
                default:
                    algorithm = new LeastMisery();
                    break;
            }
            Services.setAlgorithm(algorithm);

            // Update View
            initAlgorithmDisplay();
        });



        builder.show();
    }
}
