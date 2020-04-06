/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.exceptions.PlayerException;
import fr.pdp.mixbay.business.exceptions.SessionManagementException;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.utils.TrackAdapter;
import fr.pdp.mixbay.business.utils.UserAdapter;


public class MainActivity extends AppCompatActivity {

    private static final int MY_STORAGE_WRITE_PERMISSION = 99;

    private ListView trackListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PresentationServices.setActivity(this);

        trackListView = findViewById(R.id.trackList);

        initMainUser();

        // Ask for permission(s)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permission not granted");
            this.getWriteRights();
            Log.d("MainActivity", "Permission requested");
        } else {
            Log.d("MainActivity", "Permission granted");
            Services.CreateLogFile();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Services.onResumeApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Services.onPauseApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Services.disconnectSession();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            // Manage connection result

        }
    }

    public void initMainUser() {
        try {
            Services.requestMainUser();
            updateCurrentUserInitialAndColor();
        } catch (InterruptedException | ExecutionException e) {
            createSimpleAlertDialog(e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_STORAGE_WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Permission granted");
                    Services.CreateLogFile();
                } else {
                    Log.d("MainActivity",
                            "Permission denied by the user");
                }
                return;
            }
            default:
                break;
        }
    }

    public void updateCurrentUserInitialAndColor() {
        Button currentUser = this.findViewById(R.id.currentUserButton);
        User u = Services.getCurrentUser();

        // Set it visible
        currentUser.setVisibility(View.VISIBLE);

        // Set initial
        currentUser.setText(String.valueOf(u.initial));

        // Set color to Button
        Drawable background = currentUser.getBackground();
        ((GradientDrawable) background).setColor(u.getColor());
    }

    public void onClickPrevious(View view) {
        Services.previousMusic();
        updateCover();
    }

    public void onClickPlay(View view) {
        if (Services.isMixed())
            if (Services.isCurrentTrackPaused()) {
                view.setBackgroundResource(R.drawable
                        .baseline_pause_white_48dp);
                Services.playMusic();
            } else {
                view.setBackgroundResource(R.drawable
                        .baseline_play_arrow_white_48dp);
                Services.pauseMusic();
            }
    }

    public void onClickNext(View view) {
        Services.nextMusic();
        updateCover();
    }

    public void onClickMix(View view) {
        try {
            LocalPlaylist playlist = Services.mix();

            // Display the playlist into the ListView
            TrackAdapter adapter = new TrackAdapter(this,
                    (List<Track>) playlist.getTracks());
            trackListView.setAdapter(adapter);

            // Update Cover Image and set the play/pause button to pause
            updateCover();
            ImageButton playButton = this.findViewById(R.id.playButton);
            playButton.setBackgroundResource(R.drawable.baseline_pause_white_48dp);

        } catch (PlayerException e) {
            createSimpleAlertDialog(e.getMessage());
        }
    }

    public void onClickLike(View view) {
        if (Services.isMixed())
            if (Services.isCurrentTrackLiked()) {
                view.setBackgroundResource(R.drawable.like);
                Services.unlikeMusic();
            } else {
                view.setBackgroundResource(R.drawable.liked);
                Services.likeMusic();
            }
    }


    public void onClickSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClickAddUser(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.AlertDialogStyle);
        builder.setTitle(R.string.add_user_alert_title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Add OK button with behaviour
        builder.setPositiveButton(R.string.OK, (dialog, which) -> {
            String id = input.getText().toString();
            try {
                Services.addUserWithId(id);
            } catch (ExecutionException | InterruptedException | SessionManagementException e) {
                createSimpleAlertDialog(e.getMessage());
            }
        });

        // Add Cancel button
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        // Display the Alert
        builder.show();
    }

    private void createSimpleAlertDialog(String message) {
        AlertDialog.Builder alert = new AlertDialog
                .Builder(this, R.style.AlertDialogStyle);

        alert.setTitle(R.string.error_title);
        alert.setMessage(message);
        alert.setPositiveButton(R.string.OK, null);

        alert.show();
    }

    public void onClickCurrentUser(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.AlertDialogStyle);
        builder.setTitle(R.string.current_user_alert_title);

        final UserAdapter userAdapter = new UserAdapter(this,
                Services.getUsers());

        builder.setAdapter(userAdapter, (dialogInterface, i) -> {
            Services.setCurrentUser(((User) userAdapter.getItem(i)));
            updateCurrentUserInitialAndColor();
            dialogInterface.dismiss();
        });

        builder.show();
    }

    public void updateCover() {
        ImageView image = this.findViewById(R.id.albumPicture);
        if (Services.getSession().getCurrentTrack() != null)
            Picasso.get().load(Services.getSession().getCurrentTrack().cover_url)
                    .into(image);
    }

    public void updateLikeButton() {
        ImageButton button = this.findViewById(R.id.likeButton);
        if (Services.isCurrentTrackLiked()) {
            button.setBackgroundResource(R.drawable.liked);
        } else {
            button.setBackgroundResource(R.drawable.like);
        }
    }

    public void getWriteRights() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_STORAGE_WRITE_PERMISSION);
    }
}
