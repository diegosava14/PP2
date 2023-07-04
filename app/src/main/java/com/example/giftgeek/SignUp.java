package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
    Boolean selected = false;
    String image;

    Spinner spinner;

    ImageView backButton;
    Button signUpButton;

    EditText nameField;
    EditText surnameField;
    EditText emailField;
    EditText passwordField;

    Intent intent = new Intent();

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

        spinner = findViewById(R.id.spinner_signup);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                image = parent.getItemAtPosition(position).toString();
                selected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected = false;
            }
        });

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

                System.out.println(name.isEmpty() + " " + surname.isEmpty() + " " + email.isEmpty() + " " + password.isEmpty() + " " + !selected + " " + image.equals("Select a profile picture"));
                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()
                        || !selected || image.equals("Select a profile picture")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    String url_image = MethodsAPI.getUrlPic(image);
                    signUp(name, surname, email, password, url_image);
                }
            }
        });
    }

    public void signUp(String firstName,String lastName, String email, String password, String url_image) {
        String url = MethodsAPI.URL_REGISTER;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody = User.registerUserJson(firstName, lastName, email, password, url_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            int id = Integer.parseInt(response.getString("id"));
                            intent.putExtra("user_id", id);
                            intent.putExtra("password", password);
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
                        spinner.setSelection(0);
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