package com.example.sqls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    Button upload, my_videos;

    TextView name, id, number_of_videos, email, subs;

    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "ELearn";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;
    String ID;

    public void addData(){
        if (connection!=null){
            Statement statement = null;
            try {
                statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("Select * from Tutor where tutorID = '" + ID + "';");
                resultSet.next();

                name.setText(resultSet.getString(2));
                id.setText(resultSet.getString(3));
                email.setText(resultSet.getString(5));
                number_of_videos.setText("Number of videos : " + resultSet.getString(7));
                subs.setText("Number of subscribers : " + resultSet.getString(1));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("Status", "Connection is null");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Log.d("Status", "SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("Status", "ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Status", "FAILURE");
        }


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        ID = bundle.getString("TutorID");
        final String tutorName = bundle.getString("TutorName");

        assert ID != null;
        Log.d("Profile", ID);

        upload = findViewById(R.id.upload_button);
        my_videos = findViewById(R.id.find_my_videos_button);

        name = findViewById(R.id.name);
        id = findViewById(R.id.tutorID);
        number_of_videos = findViewById(R.id.number_videos);
        subs = findViewById(R.id.number_subscribers);
        email = findViewById(R.id.email);

        addData();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TutorName", tutorName);
                bundle.putString("TutorID", ID);
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