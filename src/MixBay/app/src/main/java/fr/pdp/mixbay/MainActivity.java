package fr.pdp.mixbay;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.application.Services;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Session;
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            // Manage connection result
            ((SpotifyAPI) Services.getSession().getApi()).onConnectionResult(response);

            requestTests();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Services.getSession().end(); // TODO Add a service
    }

    @Deprecated
    private void requestTests() {
        try {
            //User user = this.api.getUser("vilvilain").get();
            User user = this.session.getApi().getUser("216n6wqn2dkep6f6hkjw5yocq").get();
            System.out.println("id: " + user.id + " ; display_name: " + user.username);

            Set<Playlist> playlists = this.session.getApi().getUserPlaylists("216n6wqn2dkep6f6hkjw5yocq").get();
            System.out.println(playlists.toString());
            for (Playlist p : playlists) {
                System.out.println("Playlist name:" + p.name);

                for (Track t : p.getTracks()) {
                    System.out.println("\t\t " + t.title);

                }
                System.out.println("Number of tracks: " + p.getTracks().size());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
