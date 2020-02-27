package fr.pdp.mixbay.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.models.LocalPlaylist;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.exceptions.APIConnectionException;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Session;
import fr.pdp.mixbay.business.utils.TrackAdapter;
import fr.pdp.mixbay.data.SpotifyAPI;
import fr.pdp.mixbay.business.models.User;


public class MainActivity extends AppCompatActivity {

    private static final int MY_STORAGE_WRITE_PERMISSION = 99;
    private Session session;

    private ListView trackListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        trackListView = findViewById(R.id.trackList);

        // Remove the 2 following lines if LoginActivity is the launcher activity
        Services.createSpotifySession();
        Services.getSession().start(this);
        session = Services.getSession();

        //Ask for permission(s)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_WRITE_PERMISSION);
            Log.d("MainActivity", "Permission requested");
        }
        else
        {
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
            } catch (APIConnectionException e) {
                // TODO Manage exception
                System.out.println(e.getMessage());
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


    @Override
    protected void onStop() {
        super.onStop();
        Services.disconnectSession();
    }

    public void onClickPrevious(View view) {
        Services.previousMusic();
    }

    public void onClickPlay(View view) {
        Services.playMusic();
    }

    public void onClickNext(View view) {
        Services.nextMusic();
    }

    public void onClickMix(View view) {
        LocalPlaylist playlist = Services.mix();
        // will works with users set up
//        TrackAdapter a = new TrackAdapter(this, (List<Track>) playlist.getTracks());
//        trackListView.setAdapter(a);

        // For tests
        try {
            String id = "216n6wqn2dkep6f6hkjw5yocq";
            User user = this.session.getApi().getUser(id).get();
            if (user == null)
                return;

            System.out.println("id: " + user.id + " ; display_name: " + user.username);

            List<Playlist> playlists = new ArrayList<>(this.session.getApi().getUserPlaylists(user.id).get());

            for (Playlist p : playlists)
                user.addPlaylist(p);

            // Display the playlist into the ListView
            TrackAdapter adapter = new TrackAdapter(this, new ArrayList<>(playlists.get(3).getTracks()));
            trackListView.setAdapter(adapter);

        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
