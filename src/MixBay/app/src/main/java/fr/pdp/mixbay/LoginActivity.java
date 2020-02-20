package fr.pdp.mixbay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import fr.pdp.mixbay.application.Services;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.data.SpotifyAPI;

public class LoginActivity extends AppCompatActivity {

    Button spotifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spotifyButton = findViewById(R.id.btnSpotifyLogin);

        spotifyButton.setOnClickListener(view ->
                Services.createSpotifySession().start(this));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            // Manage connection result
            boolean result = ((SpotifyAPI) Services.getSession().getApi()).onConnectionResult(response);

            if (result) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
    }
}
