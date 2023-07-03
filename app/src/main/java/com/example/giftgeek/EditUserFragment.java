package com.example.giftgeek;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditUserFragment extends Fragment {
    Boolean selected = false;

    String image;
    ImageView backButton;

    Button doneButton;

    EditText newName, newLastname, newPassword;

    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        backButton = view.findViewById(R.id.backButton_edit);
        doneButton = view.findViewById(R.id.doneButton_edit);
        newName = view.findViewById(R.id.name_edit);
        newLastname = view.findViewById(R.id.lastname_edit);
        newPassword = view.findViewById(R.id.password_edit);

        spinner = view.findViewById(R.id.spinner_edit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
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
                getActivity().onBackPressed();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle userFragmentBundle = getParentFragmentManager().findFragmentByTag("UserFragment")
                        .getArguments();

                String image_url = MethodsAPI.getUrlPic(image);
                String firstName = newName.getText().toString();
                String lastName = newLastname.getText().toString();
                String password = newPassword.getText().toString();

                Log.d("Image", image);

                if (firstName.isEmpty() && lastName.isEmpty() && password.isEmpty() &&
                        (selected == false || image.equals("Select a profile picture"))) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please fill any of the fields to edit them.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(firstName.isEmpty()){
                    firstName = userFragmentBundle.getString("name");
                }
                if(lastName.isEmpty()){
                    lastName = userFragmentBundle.getString("lastname");
                }
                if(password.isEmpty()){
                    password = userFragmentBundle.getString("password");
                }
                if(selected == false || image.equals("Select a profile picture")){
                    image_url = userFragmentBundle.getString("image");
                }

                userFragmentBundle.putString("password", password);
                userFragmentBundle.putString("name", firstName);
                userFragmentBundle.putString("lastname", lastName);
                userFragmentBundle.putString("image", image_url);

                String email = userFragmentBundle.getString("email");
                editUser(firstName, lastName, email, password, image_url);
            }
        });

        return view;
    }


    public void editUser(String firstName,String lastName, String email, String password, String image_url) {
        String url = MethodsAPI.URL_REGISTER;
        String accessToken = getArguments().getString("accessToken");
        System.out.println(accessToken);

        JSONObject requestBody = new JSONObject();

        try {
            requestBody = User.registerUserJson(firstName, lastName, email, password, image_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            String name = response.getString("name");
                            Log.d("Response", name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getActivity().onBackPressed();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }
}