package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialscreen);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button signUpButton = (Button) findViewById(R.id.signupButton);

        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        SharedPreferences sharedPreferences2 = getSharedPreferences("Usernames", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(InitialScreen.this, Login.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(InitialScreen.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}