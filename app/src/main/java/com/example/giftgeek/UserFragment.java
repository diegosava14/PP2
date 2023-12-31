package com.example.giftgeek;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {
    private ImageView profilePic;
    String url_pic;
    private TextView email;
    private TextView name;
    private TextView lastname;

    private Button logoutButton;
    private Button statsButton;
    private ImageView editProfileButton;

    Bundle bundle_edit;
    Bundle bundle_stats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        profilePic = view.findViewById(R.id.profile_pic_user);

        email = view.findViewById(R.id.email_textview);
        name = view.findViewById(R.id.name_textview);
        lastname = view.findViewById(R.id.lastname_textview);

        logoutButton = view.findViewById(R.id.logoutButton);
        statsButton = view.findViewById(R.id.statsButton);
        editProfileButton = view.findViewById(R.id.editButton);

        bundle_edit = new Bundle();
        bundle_stats = new Bundle();

        name.setText(getArguments().getString("name"));
        lastname.setText(getArguments().getString("lastname"));
        email.setText(getArguments().getString("email"));

        url_pic = getArguments().getString("image");

        try{
            Picasso.with(getActivity()).load(url_pic).resize(200, 200).into(profilePic);
        }catch(Exception e){
            profilePic.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

        getNumWishLists();
        getNumGifts();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("accessToken", "");
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

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatisticsFragment statsFragment = new StatisticsFragment();
                statsFragment.setArguments(bundle_stats);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, statsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void getNumWishLists() {
        String url = MethodsAPI.getWishLists(getArguments().getInt("user_id"));
        String accessToken = getArguments().getString("accessToken");
        System.out.println(accessToken);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int numGifts = 0;
                        bundle_stats.putString("numWishLists", String.valueOf(response.length()));

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject wishList = response.getJSONObject(i);
                                JSONArray gifts = wishList.getJSONArray("gifts");
                                numGifts += gifts.length();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        bundle_stats.putString("numWishes", String.valueOf(numGifts));
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

    public void getNumGifts() {
        String url = MethodsAPI.getReservedGifts(getArguments().getInt("user_id"));
        String accessToken = getArguments().getString("accessToken");
        System.out.println(accessToken);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        bundle_stats.putString("numGifts", String.valueOf(response.length()));
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


