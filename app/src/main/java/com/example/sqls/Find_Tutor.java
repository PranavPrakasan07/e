package com.example.sqls;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Find_Tutor extends AppCompatActivity {

    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "ELearn";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;

    String query = "SELECT * FROM Tutor WHERE tutorID <> 'T000000000'";
    String condition = "";
    int number_of_results = 0;

    Button find_button;
    EditText name, email, contact, id;

    TextView filter_results;

    SeekBar open;
    int check_progress = 0;

    LinearLayout linearLayout;
    ConstraintLayout background;
    ScrollView scrollView;

    String results;

    public void find_tutor_from_filter(){

        query = "SELECT * FROM Tutor WHERE tutorID <> 'TTA0000000'";
        condition = "";

        if (connection!=null){
            Statement statement = null;
            try {
                statement = connection.createStatement();

                if (!TextUtils.isEmpty(name.getText().toString())){
                    condition += " AND tutorName = '" + name.getText().toString() + "'";
                }

                if (!TextUtils.isEmpty(email.getText().toString())){
                    condition += " AND tutorName = '" + name.getText().toString() + "'";
                }

                if (!TextUtils.isEmpty(contact.getText().toString())){
                    condition += " AND contact = '" + contact.getText().toString() + "'";
                }

                if (!TextUtils.isEmpty(id.getText().toString())){
                    condition += " AND tutorID = '" + id.getText().toString() + "'";
                }

                if (check_progress == 1){
                    condition += " AND open_to_work = 'Y'";
                }

                open.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        check_progress = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(Find_Tutor.this, "Drag the bar to check for tutors open to work", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                condition += ";";

                query += condition;

                ResultSet resultSet = statement.executeQuery(query);

                number_of_results = 0;
                results = "";

                while (resultSet.next()){
                    number_of_results++;
                    results += number_of_results + "\n" +
                            "Tutor ID : " + resultSet.getString(1) + ", \n" +
                            "Tutor Name : " + resultSet.getString(2) + ", \n" +
                            "Tutor Contact : " + resultSet.getString(3) + ", \n" +
                            "Tutor Email : " + resultSet.getString(5) + ", \n" +
                            "Working availability : " + resultSet.getString(6) + ", \n" +
                            "Number of uploads : " + resultSet.getString(7) +  "\n\n\n";

                    Log.d("Message", resultSet.getString(1));
                    Log.d("Message", resultSet.getString(2));
                    Log.d("Message", resultSet.getString(3));
                    Log.d("Message", resultSet.getString(4));
                    Log.d("Message", resultSet.getString(5));
                    Log.d("Message", resultSet.getString(6));
                    Log.d("Message", resultSet.getString(7));

                }

                filter_results.setText(results);
                Toast.makeText(this, number_of_results + " result(s) found", Toast.LENGTH_SHORT).show();

                linearLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("Message", "Connection is null");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_tutor);

        linearLayout = findViewById(R.id.view_layout);
        background = findViewById(R.id.tutor_background);

        find_button = findViewById(R.id.find_button_filter);

        filter_results = findViewById(R.id.results);

        scrollView = findViewById(R.id.scrollview);

        open = findViewById(R.id.seekBar3);

        name = findViewById(R.id.tutor_name);
        email = findViewById(R.id.tutor_email);
        contact = findViewById(R.id.tutor_phone);
        id = findViewById(R.id.tutor_id);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Log.d("Message", "SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("Message", "ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Message", "FAILURE");
        }

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_tutor_from_filter();
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
        });

        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

            }
        });

    }
}
