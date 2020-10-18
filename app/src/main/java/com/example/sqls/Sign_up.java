package com.example.sqls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Sign_up extends AppCompatActivity {

    EditText name_text, password_text, email_text, contact_text;
    Button sign_up;

    SeekBar open;

    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "ELearn";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;

    int totalUsers = 0;
    String id;
    String salt_string;
    String hash;
    char open_to_work = 'N';

    public void insert(){
        if (connection!=null){
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("Select * from Tutor;");

                try {
                    resultSet = statement.executeQuery("SELECT COUNT(*) FROM Tutor;");
                    resultSet.next();
                    totalUsers = Integer.parseInt(resultSet.getString(1));
                    Log.d("Count", String.valueOf(totalUsers));
                }catch (Exception e){
                    Log.d("Error", "Counting error");
                }

                byte[] salt = new byte[0];

                try {
                    salt = getSalt();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Log.d("Error", "Salt generating error");
                }

                try {
                    id = "TTA" + (9999999 - totalUsers);
                    salt_string = Arrays.toString(salt);
                    hash = get_SHA_1_SecurePassword(password_text.getText().toString(), salt);
                    Log.d("Hash", hash);
                }catch(Exception e){
                    Log.d("Error", "Error in try");
                }

                String query = null;
                try {
                    query = "INSERT INTO Tutor VALUES ('" + id + "', '" + name_text.getText().toString() + "', '" + contact_text.getText().toString()
                            + "','" + hash + "','" + email_text.getText().toString() + "', '" + open_to_work + "', 1, '" + salt_string + "');";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                statement.executeUpdate(query);

                while (resultSet.next()){
                    Log.d("Message", resultSet.getString(1));
                    Log.d("Message", resultSet.getString(2));
                    Log.d("Message", resultSet.getString(3));
                    Log.d("Message", resultSet.getString(4));
                    Log.d("Message", resultSet.getString(5));
                    Log.d("Message", resultSet.getString(6));
                    Log.d("Message", resultSet.getString(7));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Connection is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name_text = findViewById(R.id.name);
        password_text = findViewById(R.id.password);
        contact_text = findViewById(R.id.contact);
        email_text = findViewById(R.id.email);

        sign_up = findViewById(R.id.sign_up_button);

        open = findViewById(R.id.seekBar);

        open.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 1) {
                    open_to_work = 'Y';
                    Toast.makeText(getApplicationContext(), "Open to work", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Sign_up.this, "Drag the bar to set status as 'OPEN FOR WORK'", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



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

        ResultSet resultSet = null;
        if (connection!=null){
            Statement statement = null;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from Tutor");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
           Log.d("Message", "Connection is null");
        }


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Sign_up.this, "Verifying", Toast.LENGTH_SHORT).show();

                if (!TextUtils.isEmpty(name_text.getText().toString()) && !TextUtils.isEmpty(email_text.getText().toString())
                        && !TextUtils.isEmpty(password_text.getText().toString()) && !TextUtils.isEmpty(contact_text.getText().toString())){
                    insert();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                else{
                    Toast.makeText(Sign_up.this, "Please fill in all credentials", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private static String get_SHA_1_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

}
