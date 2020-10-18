package com.example.sqls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    Button upload, my_videos;

    TextView name, id, number_of_videos, email, subs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String ID = bundle.getString("TutorID");
        final String tutorName = bundle.getString("TutorName");

        assert ID != null;
        Log.d("Profile", ID);

        upload = findViewById(R.id.upload_button);
        my_videos = findViewById(R.id.find_my_videos_button);

        name = findViewById(R.id.name);
        id = findViewById(R.id.tutorID);

        name.setText(tutorName);
        id.setText(ID);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TutorName", tutorName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        my_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyVideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TutorID", ID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}