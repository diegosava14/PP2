package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;

public class SignUp extends AppCompatActivity {

    ImageView backButton;
    Button signUpButton;

    EditText usernameField;
    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backButton = (ImageView) findViewById(R.id.backButton_signUp);
        signUpButton = (Button) findViewById(R.id.signupButton_signup);

        usernameField = (EditText) findViewById(R.id.username_field);
        emailField = (EditText) findViewById(R.id.emailField_signUp);
        passwordField = (EditText) findViewById(R.id.passwordField_signUp);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SignUp.this, InitialScreen.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if(signUp(username, email, password)){
                    Intent intent = new Intent();
                    intent.putExtra("loggedInUser", email);
                    //intent.putExtra("Class", "SignUp");
                    intent.setClass(SignUp.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean signUp(String username, String email, String password) {
        boolean success = false;

        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        SharedPreferences sharedPreferences2 = getSharedPreferences("Usernames", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        String available = sharedPreferences.getString(email, "not found");

        Map<String, ?> allEntries = sharedPreferences2.getAll();

        boolean usernameExists = false;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String searchEmail = entry.getKey();
            String searchUsername = entry.getValue().toString();

            if (searchUsername.equalsIgnoreCase(username)) {
                usernameExists = true;
                break;
            }
        }

        if(available.equals("not found") && !usernameExists) {

            editor.putString(email, password);
            editor.commit();

            editor2.putString(email, username);
            editor2.commit();

            Toast.makeText(getApplicationContext(), "Sign Up successful!", Toast.LENGTH_SHORT).show();

            success = true;
        }

        if(!available.equals("not found") && usernameExists){
            Toast.makeText(getApplicationContext(), "Username and Email already exists", Toast.LENGTH_SHORT).show();
            usernameField.setText("");
            emailField.setText("");
            success = false;
        }

        if(!available.equals("not found") && !usernameExists){
            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
            emailField.setText("");
            success = false;
        }

        if(available.equals("not found") && usernameExists){
            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            usernameField.setText("");
            success = false;
        }

        return success;
    }
}