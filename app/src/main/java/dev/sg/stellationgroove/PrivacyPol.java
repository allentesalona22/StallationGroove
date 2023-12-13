package dev.sg.stellationgroove;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences;
        Button accept;
        Button denied;
        WebView policy;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypol);


        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        boolean accepted = preferences.getBoolean("accepted", false);
        if (accepted) {
            moveToMainActivity();
        }

        policy = findViewById(R.id.policy);
        accept = findViewById(R.id.accept);
        denied = findViewById(R.id.reject);

        policy.loadUrl("file:///android_asset/policy.html");

        accept.setOnClickListener(view -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("accepted", true);
            editor.apply();
            moveToMainActivity();
        });
        denied.setOnClickListener(view -> finishAffinity());
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(PrivacyPol.this, StellationMenu.class);
        startActivity(intent);
        finish();
    }
}