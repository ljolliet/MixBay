/**
 * Application MixBay
 *
 * @authors E. Bah, N. Deguillaume, L. Jolliet, J. Loison, P. Vigneau
 * @version 1.0
 * Génération de playlistes musicales pour un groupe d'utilisateurs
 * PdP 2019-2020 Université de Bordeaux
 */

package fr.pdp.mixbay.presentation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import fr.pdp.mixbay.R;

public class CGUActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgu);

        WebView webView = findViewById(R.id.CGUWebView);
        webView.loadUrl("file:///android_asset/CGU.html");
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickAccept(View view) {
        SharedPreferences.Editor editor =
                getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
                        .edit();
        editor.putBoolean(LoginActivity.CGU_ACCEPTED_STRING, true);
        editor.apply();

        setResult(Activity.RESULT_OK);
        finish();
    }
}
