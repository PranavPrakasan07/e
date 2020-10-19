package com.example.sqls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyVideoActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    List<Video> all_videos;
    RecyclerView videolist;
    VideoAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Button more_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String ID = bundle.getString("TutorID");

        assert ID != null;
        Log.d("VideoActivity", ID);

        database = FirebaseDatabase.getInstance();
        try {
            myRef = database.getReference().child("1").child("tutors").child(ID).child("videos");
            getData();

        }
        catch(Exception e){
            Toast.makeText(this, "Unable to find any content", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Reference not available");
        }

        more_options = findViewById(R.id.button);

        all_videos = new ArrayList<>();
        videolist = findViewById(R.id.videolist);
        videolist.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VideoAdapter(this, all_videos);
        videolist.setAdapter(adapter);

        more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Options_Menu.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {

        final int[] number_of_videos = {12};

                for (int i = 0; i< number_of_videos[0]; i++){
                    myRef.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            try {
                                number_of_videos[0] = (int) dataSnapshot.getChildrenCount() - 2;
                                Log.d("Number of videos SNAP:", String.valueOf(number_of_videos[0]));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            int count = 0;
                            Video v = new Video();

                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                                switch (count){
                                    case 0:v.setDescription(Objects.requireNonNull(dataSnapshot1.getValue()).toString());count++;break;
                                    case 1:v.setVideoURL(Objects.requireNonNull(dataSnapshot1.getValue()).toString());count++;break;
                                    case 2:v.setSubtitle(Objects.requireNonNull(dataSnapshot1.getValue()).toString());count++;break;
                                    case 3:v.setImageURL(Objects.requireNonNull(dataSnapshot1.getValue()).toString());count++;break;
                                    case 4:v.setTitle(Objects.requireNonNull(dataSnapshot1.getValue()).toString());count++;break;
                                    case 5:v.setAuthor(Objects.requireNonNull(dataSnapshot1.getValue()).toString());count++;break;
                                }
                            }

                            all_videos.add(v);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
    }
}