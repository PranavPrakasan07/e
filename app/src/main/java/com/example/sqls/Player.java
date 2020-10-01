package com.example.sqls;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Player extends AppCompatActivity {

    private Button desc_show_button;
    private ScrollView desc_scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        desc_show_button = findViewById(R.id.desc_appear_button);
        desc_scroller = findViewById(R.id.description_scroller);

        desc_show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc_show_button.getText() == "View Description"){
                    desc_show_button.setText("Hide Description");
                    desc_scroller.setVisibility(View.VISIBLE);
                }
                else {
                    desc_show_button.setText("View Description");
                    desc_scroller.setVisibility(View.GONE);
                }
            }
        });

//        TextView title = findViewById(R.id.video_title);
        TextView desc = findViewById(R.id.video_description);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final VideoView videoPlayer = findViewById(R.id.videoView);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        assert data != null;
        Video video = (Video) data.getSerializable("videoData");

        assert video != null;
//        title.setText(video.getTitle());
        desc.setText(video.getDescription());
        //getSupportActionBar().setTitle(video.getTitle());

        Uri videoUri = Uri.parse(video.getVideoURL());
        videoPlayer.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);


        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                videoPlayer.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            
        }

        return super.onOptionsItemSelected(item);
    }
}