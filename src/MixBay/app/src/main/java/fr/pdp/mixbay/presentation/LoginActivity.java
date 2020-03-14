package fr.pdp.mixbay.presentation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.dataAccess.APIManagerI;
import fr.pdp.mixbay.business.exceptions.APIConnectionException;
import fr.pdp.mixbay.data.SpotifyAPI;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "PrefsFile";
    public static final String CGU_ACCEPTED_STRING = "cgu_accepted";
    public static final int CGU_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Spotify authentication response
        if (requestCode == APIManagerI.SPOTIFY_REQUEST_CODE) {
            // Manage connection result
            try {
                Services.getSession().getApi().onConnectionResult(resultCode, intent);

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } catch (APIConnectionException e) {
                // TODO Manage exception
                System.out.println(e.getMessage());
            }
        }
        // CGU response
        else if (requestCode == CGU_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                onClickSpotify(null);
        }
    }

    public void onClickSpotify(View view) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean cguAccepted = prefs.getBoolean(CGU_ACCEPTED_STRING, false);

        if (cguAccepted)
            Services.createSpotifySession().start(this);
        else {
            Intent intent = new Intent(this, CGUActivity.class);
            startActivityForResult(intent, CGU_REQUEST_CODE);
        }
    }
}
