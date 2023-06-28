package com.example.giftgeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class UserFragment extends Fragment {
    private TextView email;
    private TextView name;
    private TextView lastname;

    private Button logoutButton;
    private Button statsButton;
    private ImageView editProfileButton;

    Bundle bundle_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        email = view.findViewById(R.id.email_textview);
        name = view.findViewById(R.id.name_textview);
        lastname = view.findViewById(R.id.lastname_textview);

        logoutButton = view.findViewById(R.id.logoutButton);
        statsButton = view.findViewById(R.id.statsButton);
        editProfileButton = view.findViewById(R.id.editButton);

        bundle_edit = new Bundle();

        setUserInfo();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), InitialScreen.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserFragment editUserFragment = new EditUserFragment();


                bundle_edit.putInt("user_id", getArguments().getInt("user_id"));
                bundle_edit.putString("accessToken", getArguments().getString("accessToken"));
                editUserFragment.setArguments(bundle_edit);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, editUserFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void setUserInfo() {
        String url = MethodsAPI.getUserById(getArguments().getInt("user_id"));
        String accessToken = getArguments().getString("accessToken");
        System.out.println(accessToken);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            name.setText(response.getString("name"));
                            lastname.setText(response.getString("last_name"));
                            email.setText(response.getString("email"));

                            bundle_edit.putString("name_OLD", response.getString("name"));
                            bundle_edit.putString("lastname_OLD", response.getString("last_name"));
                            bundle_edit.putString("email_OLD", response.getString("email"));
                            bundle_edit.putString("password_OLD", response.getString("password"));
                            bundle_edit.putString("image_OLD", getArguments().getString("image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
