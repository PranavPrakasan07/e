package com.example.sqls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Options_Menu extends AppCompatActivity {

    Button user_login, tutor_login;
    Button find_tutor, find_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options__menu);

        user_login = findViewById(R.id.create_user_profile);
        tutor_login = findViewById(R.id.create_tutor_profile);
        find_tutor = findViewById(R.id.find_tutor);
        find_video = findViewById(R.id.find_video);

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login_user.class);
                startActivity(intent);
            }
        });

        tutor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        find_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Find_Tutor.class));
            }
        });

        find_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VideoMainActivity.class));
            }
        });
    }
}