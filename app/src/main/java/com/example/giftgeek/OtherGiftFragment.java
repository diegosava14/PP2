package com.example.giftgeek;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.Entities.Wishlist;
import com.example.giftgeek.RecyclerView.GiftAdapter;
import com.example.giftgeek.RecyclerView.OtherGiftAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherGiftFragment extends Fragment implements OtherGiftAdapter.OnGiftReserveListener {
    Gift gift;
    private TextView wishlistTitleTextView;
    private RecyclerView giftRecyclerView;
    private OtherGiftAdapter giftAdapter;
    private Wishlist wishlist;

    private ImageView backButton;
    private List<Gift> giftList;

    public static OtherGiftFragment newInstance(Wishlist wishlist) {
        OtherGiftFragment fragment = new OtherGiftFragment();
        Bundle args = new Bundle();
        args.putParcelable("wishlist", wishlist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wishlist = getArguments().getParcelable("wishlist");
            giftList = wishlist.getGifts();
            // Assuming you have a method to get the list of gifts from the Wishlist object

        } else {
            giftList = new ArrayList<>(); // Initialize an empty list
        }
        giftAdapter = new OtherGiftAdapter(getActivity(), giftList, requireContext());
        giftAdapter.setOnGiftReserveListener(this);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_gift, container, false);
        wishlistTitleTextView = view.findViewById(R.id.otherWishlistTitleTextView);
        giftRecyclerView = view.findViewById(R.id.otherGiftRecyclerView);

        wishlistTitleTextView.setText(wishlist.getName());
        backButton = view.findViewById(R.id.backButton_otherGift);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wishlistTitleTextView.setText(wishlist.getName());

        giftRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        giftRecyclerView.setAdapter(giftAdapter);

        loadGifts();
    }

    private void loadGifts() {
        String url = MethodsAPI.URL_BASE + "gifts";
        String token = getToken();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            giftList.clear(); // Clear the existing gift list

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject giftObject = response.getJSONObject(i);
                                int giftId = giftObject.getInt("id");
                                int wishlistId = giftObject.getInt("wishlist_id");
                                String productUrl = giftObject.getString("product_url");
                                String priority;
                                if (giftObject.isNull("priority")) {
                                    priority = "0"; // Assign a default value or handle the null case
                                } else {
                                    priority = giftObject.getString("priority");
                                }

                                int priorityInt = Integer.parseInt(priority);

                                int booked_int = giftObject.getInt("booked");

                                boolean booked = false;
                                if (booked_int == 1) {
                                    booked = true;
                                }

                                Gift gift = new Gift(giftId, wishlistId, productUrl, priorityInt, booked);
                                if (gift.getWishlistId() == wishlist.getWishlistId()) {
                                    loadProduct(gift);
                                }
                            }
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

    public void loadProduct(Gift gift) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, gift.getProductUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            gift.setName(response.getString("name"));
                            gift.setImageUrl(response.getString("photo"));
                            gift.setPrice(response.getDouble("price"));
                            gift.setDescription(response.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(gift.getName());
                        giftList.add(gift);
                        giftAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }
    @Override
    public void onGiftReserved(Gift gift) {
        if (gift.isBooked()) {
            Toast.makeText(getActivity(), "Gift already reserved", Toast.LENGTH_SHORT).show();
        }
        else {
            reserveGift(gift);
        }
    }

    public void reserveGift(Gift gift) {
        String url = MethodsAPI.URL_BASE + "gifts/" + gift.getId() + "/book";
        String token = getToken();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Gift has been reserved", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Could not reserve this gift, please check the wishlists expiry date", Toast.LENGTH_SHORT).show();
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

    private int getUserId() {
        int userId = -1; // Default value if ID is not found
        if (getActivity() != null && getActivity().getIntent() != null) {
            userId = getActivity().getIntent().getIntExtra("user_id", -1);
        }
        return userId;
    }

    private String getToken() {
        String token = ""; // Default value if token is not found
        if (getActivity() != null && getActivity().getIntent() != null) {
            token = getActivity().getIntent().getStringExtra("accessToken");
        }
        return token;
    }
}