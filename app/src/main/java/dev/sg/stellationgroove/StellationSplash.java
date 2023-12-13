package dev.sg.stellationgroove;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class StellationSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_stellationsplash);

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + File.separator + R.raw.stellationsplash;

        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setOnCompletionListener(mp -> {
            Intent intent = new Intent(StellationSplash.this, PrivacyPol.class);
            startActivity(intent);
            finish();
        });

        videoView.start();
    }
}