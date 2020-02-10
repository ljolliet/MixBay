package fr.pdp.mixbay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import fr.pdp.mixbay.models.APIManagerI;
import fr.pdp.mixbay.models.Session;
import fr.pdp.mixbay.models.SpotifyAPI;


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
                    this.api.getUser("vilvilain");
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
