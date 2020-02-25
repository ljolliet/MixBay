package fr.pdp.mixbay.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Set;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Remove the 2 following lines if LoginActivity is the launcher activity
        Services.createSpotifySession();
        Services.getSession().start(this);

        session = Services.getSession();
        //requestTests();

        //TODO getting a right list of tracks. the list witch is used is just for test
        // replacing icons by rights icons in footer.xm

        ListView view = findViewById(R.id.trackList);
        TrackAdapter adapter = new TrackAdapter(this, Track.getSampleTracks());
        view.setAdapter(adapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            // Manage connection result
            try {
                ((SpotifyAPI) Services.getSession().getApi()).onConnectionResult(response);

                requestTests();
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

    @Deprecated
    private void requestTests() {
        try {
            //User user = this.api.getUser("vilvilain").get();
            String id = "216n6wqn2dkep6f6hkjw5yocq";

            User user = this.session.getApi().getUser(id).get();
            if (user == null)
                return;

            System.out.println("id: " + user.id + " ; display_name: " + user.username);

            Set<Playlist> playlists = this.session.getApi().getUserPlaylists(user.id).get();
            System.out.println(playlists.toString());
            for (Playlist p : playlists) {
                System.out.println("Playlist name:" + p.name);

                for (Track t : p.getTracks()) {
                    System.out.println("\t\t " + t.title + " ; danceability: " + (t.getFeatures() == null ? "null" : t.getFeatures().allFeatures.get(TrackFeatures.Name.DANCEABILITY)));

                }
                System.out.println("Number of tracks: " + p.getTracks().size());
            }
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}