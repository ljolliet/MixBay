package fr.pdp.mixbay.presentation;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import fr.pdp.mixbay.R;
import fr.pdp.mixbay.business.services.Services;
import fr.pdp.mixbay.business.utils.UserSettingsAdapter;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        ListView userListView = findViewById(R.id.userListView);

        setActionBar(toolbar);

        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        // Set onClick event on toolbar
        toolbar.setNavigationOnClickListener(view -> finish());

        // Add Users to ListView
        UserSettingsAdapter adapter = new UserSettingsAdapter(this, new ArrayList<>(Services.getSession().getUsers()));
        userListView.setAdapter(adapter);

        // TODO Add on click listener on the "more button"
    }
}
