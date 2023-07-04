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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    ImageView backButton;
    Button logInButton;

    EditText emailField;
    EditText passwordField;

    Intent intent = new Intent();

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
                            intent.putExtra("accessToken", accessToken);
                            intent.putExtra("password", password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getId(email);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Wrong email or password.", Toast.LENGTH_SHORT).show();
                        emailField.setText("");
                        passwordField.setText("");
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
        }

    public void getId(String email) {
        String url = MethodsAPI.getUserByString(email);
        String accessToken = intent.getStringExtra("accessToken");
        System.out.println(accessToken);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject userObject = response.getJSONObject(0);
                            int id = Integer.parseInt(userObject.getString("id"));
                            intent.putExtra("user_id", id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        intent.setClass(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}