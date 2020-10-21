package com.example.sqls;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {

    private TextView textView, name_message;
    Button button;

    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "ELearn";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;

    Button send;
    EditText title, vlink, ilink, domain, description;
    String tutorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String tutorName = bundle.getString("TutorName");
        tutorID = bundle.getString("TutorID");

        assert tutorName != null;
        Log.d("Upload Activity", tutorName);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Log.d("Status", "Success");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("Status", "Error");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Status", "Failure");
        }

        send = findViewById(R.id.send_button);

        name_message = findViewById(R.id.HiMessage);
        title = findViewById(R.id.upload_video_title);
        domain = findViewById(R.id.upload_domain);
        vlink = findViewById(R.id.upload_video_link);
        ilink = findViewById(R.id.thumbnail_link);
        description = findViewById(R.id.upload_description);

        name_message.setText("Hi " + tutorName + "!");

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    send_Request();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void send_Request() throws SQLException {

        String query;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);


        if (connection!=null){
            Statement statement = null;

            String upload_date = formattedDate;

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Videos");
            resultSet.next();

            String total_vid = "VVA000000" + resultSet.getString(1);
            Log.d("Total number vids", total_vid);

            Log.d("Total number vids", tutorID);
            Log.d("Total number vids", formattedDate);

            try {
                statement = connection.createStatement();
                query = "INSERT INTO Videos VALUES ('" + tutorID + "', '" + total_vid + "', '" + title.getText().toString() + "', '" +
                        ilink.getText().toString() + "', '" + vlink.getText().toString() + "', '" + upload_date + "', '" + domain.getText().toString() +
                        "', '" + tutorID + "', '" + description.getText().toString() + "', 'N');";
                statement.executeUpdate(query);

                send.setBackgroundResource(R.drawable.correct_data);
                send.setText("Sent");
                Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("Message", "Connection is null");
        }
    }
}