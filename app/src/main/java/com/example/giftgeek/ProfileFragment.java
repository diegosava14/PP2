package com.example.giftgeek;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.RecyclerView.OtherWishListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {


    private TextView name;
    private TextView lastName;
    private TextView email;


    private Button statsButton;
    private Button wishlistsButton;
    private Button reservedGiftsButton;

    private ImageView backButton;

    Bundle stats_bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_user, container, false);

        name = view.findViewById(R.id.nameTextView);
        lastName = view.findViewById(R.id.lastNameTextView);
        email = view.findViewById(R.id.emailTextView);

        statsButton = view.findViewById(R.id.statisticsButton);
        wishlistsButton = view.findViewById(R.id.wishlistsButton);
        reservedGiftsButton = view.findViewById(R.id.reservedGiftsButton);
        backButton = view.findViewById(R.id.backButton_profile);

        stats_bundle = new Bundle();
        getNumWishLists();
        getNumGifts();

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsFragment statsFragment = new StatisticsFragment();
                statsFragment.setArguments(stats_bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, statsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Bundle wishListBundle = new Bundle();
        wishListBundle.putInt("other_user_id", getArguments().getInt("other_user_id"));
        wishListBundle.putString("accessToken", getArguments().getString("accessToken"));
        wishlistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherWishlistsFragment otherWishlistsFragment = new OtherWishlistsFragment();
                otherWishlistsFragment.setArguments(wishListBundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, otherWishlistsFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        reservedGiftsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        setProfileInformation();
        return view;
    }

    void setProfileInformation() {


        System.out.println(getArguments().getString("other_user_name"));
        System.out.println(getArguments().getString("other_user_last"));
        System.out.println(getArguments().getString("other_user_email"));

       name.setText(getArguments().getString("other_user_name"));
       lastName.setText(getArguments().getString("other_user_last"));
       email.setText(getArguments().getString("other_user_email"));

    }

    public void getNumWishLists() {
        String url = MethodsAPI.getWishLists(getArguments().getInt("other_user_id"));
        String accessToken = getArguments().getString("accessToken");
        System.out.println(accessToken);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int numGifts = 0;
                        stats_bundle.putString("numWishLists", String.valueOf(response.length()));

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject wishList = response.getJSONObject(i);
                                JSONArray gifts = wishList.getJSONArray("gifts");
                                numGifts += gifts.length();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        stats_bundle.putString("numWishes", String.valueOf(numGifts));
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
        String url = MethodsAPI.getReservedGifts(getArguments().getInt("other_user_id"));
        String accessToken = getArguments().getString("accessToken");
        System.out.println(accessToken);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        stats_bundle.putString("numGifts", String.valueOf(response.length()));
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
