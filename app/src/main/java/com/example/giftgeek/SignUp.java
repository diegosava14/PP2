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
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                signUp("Manuel", "Lopez", "manu@gmail.com", "123456");
            }
        });
    }

    public void signUp(String firstName,String lastName, String email, String password) {
        String url = MethodsAPI.URL_REGISTER;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody = User.registerUserJson(firstName, lastName, email, password, "image");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            int id = response.getInt("id");
                            System.out.println(id);
                            // Process the access token
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error: " + error);
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}