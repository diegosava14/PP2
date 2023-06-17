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

public class SignUp extends AppCompatActivity {

    ImageView backButton;
    Button signUpButton;

    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backButton = (ImageView) findViewById(R.id.backButton_signUp);
        signUpButton = (Button) findViewById(R.id.signupButton_signup);

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
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if(signUp(email, password)){
                    Toast.makeText(getApplicationContext(), "Sign Up successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(SignUp.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                    emailField.setText("");
                    passwordField.setText("");
                }
            }
        });
    }

    public boolean signUp(String email, String password) {
        boolean success;
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String available = sharedPreferences.getString(email, "not found");

        if(available.equals("not found")) {

            editor.putString(email, password);
            editor.commit();

            success = true;
        } else {
            success = false;
        }


        return success;
    }
}