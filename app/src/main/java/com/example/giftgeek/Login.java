package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    ImageView backButton;
    Button logInButton;

    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = (ImageView) findViewById(R.id.backButton_login);
        logInButton = (Button) findViewById(R.id.loginButton_login);

        emailField = (EditText) findViewById(R.id.emailField_login);
        passwordField = (EditText) findViewById(R.id.passwordField_login);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Login.this, InitialScreen.class);
                startActivity(intent);
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if(login(email, password)) {
                    Toast.makeText(getApplicationContext(), "LogIn successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("loggedInUser", email);
                    intent.setClass(Login.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Wrong email or password.", Toast.LENGTH_SHORT).show();
                    emailField.setText("");
                    passwordField.setText("");
                }
            }
        });
    }

    public boolean login(String email, String password) {
        boolean success;
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);

        String pass = sharedPreferences.getString(email, "not found");

        if (pass.equals("not found")) {
            success = false;
        }else success = password.equals(pass);

        return success;
    }
}