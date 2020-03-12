package fr.pdp.mixbay.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

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
        finish();
    }
}
