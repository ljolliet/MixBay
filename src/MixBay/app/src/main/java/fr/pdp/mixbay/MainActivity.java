package fr.pdp.mixbay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.models.Playlist;
import fr.pdp.mixbay.business.models.Session;
import fr.pdp.mixbay.data.SpotifyAPI;
import fr.pdp.mixbay.business.models.Track;
import fr.pdp.mixbay.business.models.User;


public class MainActivity extends AppCompatActivity {

    private Session session;
    private APIManagerI api;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        api = new SpotifyAPI();
        session = new Session(api);
        session.start(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == api.SPOTIFY_REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("MainActivity", "The token is: " + response.getAccessToken());
                    this.api.setAccessToken(response.getAccessToken());

                    try {
                        //User user = this.api.getUser("vilvilain").get();
                        User user = this.api.getUser("216n6wqn2dkep6f6hkjw5yocq").get();
                        System.out.println("id: " + user.id + " ; display_name: " + user.username);

                        Set<Playlist> playlists = this.api.getUserPlaylists("216n6wqn2dkep6f6hkjw5yocq").get();
                        System.out.println(playlists.toString());
                        for (Playlist p: playlists) {
                            System.out.println("Playlist name:" + p.name);

                            for (Track t: p.getTracks()) {
                                System.out.println("\t\t " + t.title);

                            }
                            System.out.println("Number of tracks: " + p.getTracks().size());
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                // Auth flow returned an error
                case ERROR:
                    // TODO Handle error response
                    Log.d("MainActivity", "The error is: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // TODO Handle other cases
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        session.end();
    }

}
