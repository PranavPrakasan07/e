package com.example.sqls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class VideoMainActivity extends AppCompatActivity {

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

        database = FirebaseDatabase.getInstance();
        try {
            myRef = database.getReference().child("1").child("tutors").child("0").child("videos");
        }
        catch(Exception e){
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

        getData();
//
//        StudentDB studentDB = new StudentDB(this);
//        SQLiteDatabase database = studentDB.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        int row;
//
//        contentValues.put("sid", 1);
//        contentValues.put("sname", "Szn");
//        contentValues.put("smarks", 234);
//
//        row = (int)database.insert("student", null, contentValues);
//
//        contentValues.put("sid", 2);
//        contentValues.put("sname", "Sweerwrzn");
//        contentValues.put("smarks", 1234);
//
//        row = (int)database.insert("student", null, contentValues);
//
//        Log.d("Message", Integer.toString(row));
//
//        SQLiteDatabase database1 = studentDB.getReadableDatabase();
//        String[] projection = {"sid", "sname", "smarks"};
//
//        Cursor cursor = database1.query("student", projection,null, null, null, null, null);
//
//        for (int i=0; i<4; i++)
//        {
//            cursor.moveToPosition(i);
//            Log.d("Name", cursor.getString(1));
//        }

    }

    private void getData() {

        final int[] count = {0};
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

                            count[0] = 0;
                            Video v = new Video();


                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                                switch (count[0]){
                                    case 0:v.setDescription(Objects.requireNonNull(dataSnapshot1.getValue()).toString());
                                        count[0]++;break;
                                    case 1:v.setVideoURL(Objects.requireNonNull(dataSnapshot1.getValue()).toString());
                                        count[0]++;break;
                                    case 2:v.setSubtitle(Objects.requireNonNull(dataSnapshot1.getValue()).toString());
                                        count[0]++;break;
                                    case 3:v.setImageURL(Objects.requireNonNull(dataSnapshot1.getValue()).toString());
                                        count[0]++;break;
                                    case 4:v.setTitle(Objects.requireNonNull(dataSnapshot1.getValue()).toString());
                                        count[0]++;break;
                                    case 5:v.setAuthor(Objects.requireNonNull(dataSnapshot1.getValue()).toString());
                                        count[0]++;break;

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

    /*
    private void getJsonData() {
        String URL = "https://raw.githubusercontent.com/PranavPrakasan07/E-Learning/master/datafile.json?token=AOC7BBH2QJCJ75IXQMKOFG27OWY62";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray categories = null;
                try {
                    categories = response.getJSONArray("categories");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject categoriesData = null;
                try {
                    assert categories != null;
                    categoriesData = categories.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray videos = null;
                try {
                    assert categoriesData != null;
                    videos = categoriesData.getJSONArray("videos");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                assert videos != null;
                for (int i = 0; i<videos.length(); i++){
                    JSONObject video = null;
                    try {
                        video = videos.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert video != null;
                        Log.d(TAG, "onResponse" + video.getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Video v = new Video();
                    try {
                        v.setTitle(video.getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        v.setAuthor(video.getString("subtitle"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        v.setDescription(video.getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        v.setImageURL(video.getString("thumb"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray videoURL = null;
                    try {
                        videoURL = video.getJSONArray("sources");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    assert videoURL != null;
                    //gs://loginsignup-d02d5.appspot.com/WIN_20200826_00_03_14_Pro.mp4
                    //videoURL.getString(0)
                    v.setVideoURL("https://firebasestorage.googleapis.com/v0/b/loginsignup-d02d5.appspot.com/o/WIN_20200826_00_03_14_Pro.mp4?alt=media&token=e9689bd6-01b2-4dca-8027-62e609056baa");

                    all_videos.add(v);

                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "onResponse" + v.getVideoURL()); 

                }


                Log.d(TAG, "onResponse" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error message " + error.getMessage());
            }
        });

        requestQueue.add(objectRequest);
    }


     */
}