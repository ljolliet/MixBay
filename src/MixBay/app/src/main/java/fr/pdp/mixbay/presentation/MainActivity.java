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
import fr.pdp.mixbay.business.exceptions.APIConnectionException;
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

        // Remove the 2 following lines if LoginActivity is the launcher activity
        Services.createSpotifySession();
        Services.getSession().start(this);

        // Ask for permission(s)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_WRITE_PERMISSION);
            Log.d("MainActivity", "Permission requested");
        }
        else {
            Log.d("MainActivity", "Permission granted");
            Services.CreateLogFile();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            // Manage connection result
            try {
                Services.getSession().getApi().onConnectionResult(resultCode, intent);
                Services.requestMainUser();
                updateCurrentUserInitialAndColor();

            } catch (APIConnectionException e) {
                // TODO Manage exception
                System.err.println("API connection error: " + e.getMessage());
            } catch (InterruptedException | ExecutionException e) {
                // TODO Manage exception
                System.err.println("Requesting main user error: " + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_STORAGE_WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Permission granted");
                    Services.CreateLogFile();
                } else {
                    Log.d("MainActivity", "Permission denied by the user"); //TODO manage this case in LogManager
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


    @Override
    protected void onStop() {
        super.onStop();
        Services.disconnectSession();
    }

    public void onClickPrevious(View view) {
        Services.previousMusic();
        updateCover();
    }

    public void onClickPlay(View view) {
        Services.playMusic();
    }

    public void onClickNext(View view) {
        Services.nextMusic();
        updateCover();
    }

    public void onClickMix(View view) {
        LocalPlaylist playlist = Services.mix();

        // Display the playlist into the ListView
        TrackAdapter adapter = new TrackAdapter(this, (List<Track>) playlist.getTracks());
        trackListView.setAdapter(adapter);
        updateCover();
    }

    public void onClickLike(View view) {
        if(Services.isMixed())
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(R.string.add_user_alert_title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Add OK button with behaviour
        builder.setPositiveButton(R.string.OK, (dialog, which) -> {
            String id = input.getText().toString();
            try {
                Services.addUserWithId(id);
            } catch (ExecutionException | InterruptedException e) {
                // TODO Manage exception
                System.out.println("Requesting user error: " + e.getMessage());
                e.printStackTrace();
            } catch (SessionManagementException e) {
                // TODO Manage exception
                System.out.println("Session management error: " + e.getMessage());
            }
        });

        // Add Cancel button
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        // Display the Alert
        builder.show();
    }

    public void onClickCurrentUser(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(R.string.current_user_alert_title);

        final UserAdapter userAdapter = new UserAdapter(this, Services.getUsers());

        builder.setAdapter(userAdapter, (dialogInterface, i) -> {
            Services.setCurrentUser(((User) userAdapter.getItem(i)));
            updateCurrentUserInitialAndColor();
            dialogInterface.dismiss();
        });

        builder.show();
    }

    public void updateCover() {
        ImageView image = this.findViewById(R.id.albumPicture);
        if(Services.getSession().getCurrentTrack() != null)
            Picasso.get().load(Services.getSession().getCurrentTrack().cover_url).into(image);
    }

    public void updateLikeButton() {
        ImageButton button = this.findViewById(R.id.likeButton);
        if (Services.isCurrentTrackLiked()) {
            button.setBackgroundResource(R.drawable.liked);
        } else {
            button.setBackgroundResource(R.drawable.like);
        }
    }
}
