package com.example.giftgeek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.Entities.Wishlist;
import com.example.giftgeek.RecyclerView.OtherWishListAdapter;
import com.example.giftgeek.RecyclerView.ReservedGiftAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservedGiftsFragment extends Fragment {

    private RecyclerView reservedGiftsRecyclerView;
    private ReservedGiftAdapter reservedGiftAdapter;
    private List<Gift> gifts;

    private ImageView backButton;
    private TextView reservedGiftsTitle;

    public ReservedGiftsFragment() {
        // Required empty public constructor
    }

    public static ReservedGiftsFragment newInstance() {
        return new ReservedGiftsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserved_gift, container, false);

        reservedGiftsRecyclerView = view.findViewById(R.id.reservedGiftsRecyclerView);

        // Set up RecyclerView
        gifts = new ArrayList<>();
        reservedGiftAdapter = new ReservedGiftAdapter(gifts, getArguments().getInt("other_user_id"));
        reservedGiftsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        reservedGiftsRecyclerView.setAdapter(reservedGiftAdapter);

        backButton = view.findViewById(R.id.backButton_reservedGifts);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Load wishlists from API
        loadGifts();
        return view;
    }

    public void loadGifts() {
        String url = MethodsAPI.URL_BASE + "users/" + getArguments().getInt("other_user_id") + "/gifts/reserved";
        String token = getArguments().getString("accessToken");

       // url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/1/gifts/reserved";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            gifts.clear(); // Clear the existing wishlists

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject giftObject = response.getJSONObject(i);

                                int giftId = giftObject.getInt("id");
                                int wishlistId = giftObject.getInt("wishlist_id");
                                String productUrl = giftObject.getString("product_url");
                                int priorityInt;
                                if (giftObject.isNull("priority")) {
                                    priorityInt = 0; // Assign a default value or handle the null case
                                } else {
                                    priorityInt = giftObject.getInt("priority");
                                }
                                String priority;
                                if (priorityInt >= 0 && priorityInt < 4) {
                                    priority = "LOW";
                                } else {
                                    if (priorityInt >= 4 && priorityInt < 7) {
                                        priority = "MEDIUM";
                                    } else {
                                        if (priorityInt >= 7 && priorityInt <= 10) {
                                            priority = "HIGH";
                                        } else {
                                            priority = "LOW";
                                        }
                                    }
                                }
                                int booked_int = giftObject.getInt("booked");

                                boolean booked = false;
                                if (booked_int == 1) {
                                    booked = true;
                                }

                                Gift gift = new Gift(giftId, wishlistId, productUrl, priority, booked);
                                gifts.add(gift);
                                //System.out.println();

                            }

                            reservedGiftAdapter.notifyDataSetChanged(); // Notify the adapter about the updated data
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token); // Include the token in the request headers
                return headers;
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }
}