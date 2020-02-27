package fr.pdp.mixbay.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.exceptions.APIConnectionException;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Session;
import fr.pdp.mixbay.business.models.TrackFeatures;
import fr.pdp.mixbay.business.utils.TrackAdapter;
import fr.pdp.mixbay.data.SpotifyAPI;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;


public class MainActivity extends AppCompatActivity {

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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            // Manage connection result
            try {
                ((SpotifyAPI) Services.getSession().getApi()).onConnectionResult(response);
            } catch (APIConnectionException e) {
                // TODO Manage exception
                System.out.println(e.getMessage());
            }
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
        Services.mix();

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
