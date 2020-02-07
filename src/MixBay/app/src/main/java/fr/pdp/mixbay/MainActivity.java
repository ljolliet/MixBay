package fr.pdp.mixbay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fr.pdp.mixbay.models.APIManagerI;
import fr.pdp.mixbay.models.Session;
import fr.pdp.mixbay.models.SpotifyAPI;


public class MainActivity extends AppCompatActivity {

    private Session session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        APIManagerI api = new SpotifyAPI();
        session = new Session(api);
        session.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        session.end();
    }

}
