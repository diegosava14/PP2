package com.example.giftgeek;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EditUserFragment extends Fragment {

    ImageView backButton;

    Button doneButton;

    EditText newName, newLastname, newPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        backButton = view.findViewById(R.id.backButton_edit);
        doneButton = view.findViewById(R.id.doneButton_edit);
        newName = view.findViewById(R.id.name_edit);
        newLastname = view.findViewById(R.id.lastname_edit);
        newPassword = view.findViewById(R.id.password_edit);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        newName.setText("");
                        newLastname.setText("");
                        newPassword.setText("");
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }
}