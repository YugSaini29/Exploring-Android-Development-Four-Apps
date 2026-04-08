package com.example.vintagevideaaudioplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    TextView fileNameLabel, noSignalLabel;
    EditText urlInput;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    playMedia(uri);
                    // Extract filename from URI
                    String path = uri.getPath();
                    fileNameLabel.setText(path.substring(path.lastIndexOf('/') + 1));
                }
            }
    );

    private void playMedia(Uri uri) {
        videoView.setVideoURI(uri);

        // This listener is CRITICAL for streaming
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                noSignalLabel.setVisibility(View.GONE);
                videoView.start(); // Only start once buffered!
            }
        });

        // Add this to catch errors (like 404 or bad codec)
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                fileNameLabel.setText("Error: Can't play this link");
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        fileNameLabel = findViewById(R.id.fileNameLabel);
        noSignalLabel = findViewById(R.id.noSignalLabel);
        urlInput = findViewById(R.id.urlInput);

        Button openFileBtn = findViewById(R.id.openFileBtn);
        Button openUrlBtn = findViewById(R.id.openUrlBtn);
        Button playBtn = findViewById(R.id.playBtn);
        Button pauseBtn = findViewById(R.id.pauseBtn);

        openFileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*"); //
            filePickerLauncher.launch(intent);
        });

        openUrlBtn.setOnClickListener(v -> {
            String url = urlInput.getText().toString();
            if (!url.isEmpty()) {
                playMedia(Uri.parse(url));
                fileNameLabel.setText("Streaming...");
            }
        });

        // play and paude
        playBtn.setOnClickListener(v -> videoView.start());
        pauseBtn.setOnClickListener(v -> videoView.pause());
        findViewById(R.id.stopBtn).setOnClickListener(v -> {
            videoView.stopPlayback();
            videoView.setVideoURI(null);
            videoView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            noSignalLabel.setVisibility(View.VISIBLE);
        });
        findViewById(R.id.restartBtn).setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}