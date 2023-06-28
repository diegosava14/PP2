package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    EditText nameField;
    EditText surnameField;
    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backButton = (ImageView) findViewById(R.id.backButton_signUp);
        signUpButton = (Button) findViewById(R.id.signupButton_signup);

        nameField = (EditText) findViewById(R.id.name_field);
        surnameField = (EditText) findViewById(R.id.surname_field);
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
                String name = nameField.getText().toString();
                String surname = surnameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                signUp(name, surname, email, password);
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

        Intent intent = new Intent();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            int id = response.getInt("id");
                            Log.d("user_id", String.valueOf(id));
                            intent.putExtra("user_id", id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        signIn(email, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "User already exists.", Toast.LENGTH_SHORT).show();
                        nameField.setText("");
                        surnameField.setText("");
                        emailField.setText("");
                        passwordField.setText("");
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    public void signIn(String email, String password) {
        String url = MethodsAPI.URL_LOGIN;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody = User.signInUserJson(email, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            String accessToken = response.getString("accessToken");
                            Log.d("accessToken", accessToken);
                            intent.putExtra("accessToken", accessToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        intent.setClass(SignUp.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}