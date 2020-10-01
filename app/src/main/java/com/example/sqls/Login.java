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

public class Login extends AppCompatActivity {

    EditText password_text, email_text;
    Button login, signup;

    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "ELearn";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;

    String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password_text = findViewById(R.id.password);
        email_text = findViewById(R.id.email);

        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.sign_up_button);


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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Sign_up.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Clicked", Toast.LENGTH_SHORT).show();

                if (!TextUtils.isEmpty(email_text.getText().toString()) && !TextUtils.isEmpty(password_text.getText().toString())){
                    check();
                }
                else{
                    Toast.makeText(Login.this, "Please fill in all credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void check(){
        if (connection!=null){
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("Select * from Tutor WHERE email = '" + email_text.getText().toString() + "';");

                byte[] salt = new byte[16];

                while (resultSet.next()){

                    Log.d("Message", resultSet.getString(5));

                    if (resultSet.getString(5).equals(email_text.getText().toString())) {

                        int len = resultSet.getString(8).length();
                        String res = resultSet.getString(8).substring(1, len-1);
                        String[] array = res.split(", ");

                        Log.d("String array", Arrays.toString(array));

                        for (String s : array) {
                            Log.d("Array", s);
                        }

                        for (int i=0; i < array.length; i++) {
                            salt[i] = Byte.parseByte(array[i]);
                        }

                        Log.d("Byte array", Arrays.toString(salt));

                        try {
                            hash = get_SHA_1_SecurePassword(password_text.getText().toString(), salt);
                            Log.d("Hash", hash);
                        } catch (Exception e) {
                            Log.d("Error", "Error in try");
                        }

                        if (hash.equals(resultSet.getString(4))){
                            Toast.makeText(this, "Correct Password", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                    }

                }

                Toast.makeText(this, "Inorrect Password", Toast.LENGTH_SHORT).show();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Connection is null", Toast.LENGTH_SHORT).show();
        }
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
