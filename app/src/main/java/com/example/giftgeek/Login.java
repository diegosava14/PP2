package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

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

                signIn(email, password);
            }
        });
    }

    public void signIn(String email, String password) {
        String url = MethodsAPI.URL_LOGIN;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody = User.signInUserJson(email, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            String accessToken = response.getString("accessToken");
                            System.out.println(accessToken);
                            // Process the access token
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("hola");
                            System.out.println(error);
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
        }
}