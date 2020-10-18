package com.example.sqls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindVideo extends AppCompatActivity {

    Button find_button;
    EditText find_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_video);

        find_button = findViewById(R.id.buttonfind);
        find_text = findViewById(R.id.tutortextfind);

        find_button.setOnClickListener(new View.OnClickListener() {

            String id = "0";

            @Override
            public void onClick(View v) {

                id = find_text.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("TutorID", id);
                Intent intent = new Intent(getApplicationContext(), MyVideoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}