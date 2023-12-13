package dev.sg.stellationgroove;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StellationMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton playBtn;
        ImageButton policyBtn;
        ImageButton quitBtn;

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_stellation_menu);

        playBtn = findViewById(R.id.playButton);
        policyBtn = findViewById(R.id.policyButton);
        quitBtn = findViewById(R.id.quitButton);

        playBtn.setOnClickListener(v -> moveToMainActivity());
        policyBtn.setOnClickListener(v -> showAppearingBox());
        quitBtn.setOnClickListener(v -> finishAffinity());

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showAppearingBox() {

        WebView policy;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = LayoutInflater.from(StellationMenu.this).inflate(R.layout.policy, null);

        policy = dialogView.findViewById(R.id.policyView);

        WebSettings webSettings = policy.getSettings();
        webSettings.setJavaScriptEnabled(true);

        policy.loadUrl("file:///android_asset/policy.html");
        builder.setView(dialogView);
        builder.setPositiveButton("Accept" ,(dialog,which) -> dialog.dismiss()).show();

    }

    private void moveToMainActivity() {
        Intent intent = new Intent(StellationMenu.this, StellationMain.class);
        startActivity(intent);
        finish();
    }
}