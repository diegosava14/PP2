package com.example.giftgeek;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
import com.example.giftgeek.RecyclerView.OtherGiftAdapter;
import com.example.giftgeek.RecyclerView.OtherWishListAdapter;
import com.example.giftgeek.RecyclerView.WishListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class OtherWishlistsFragment extends Fragment {

    private RecyclerView wishlistRecyclerView;
    private OtherWishListAdapter wishlistAdapter;
    private List<Wishlist> wishlist;

    ImageView backButton;

    public OtherWishlistsFragment() {
        // Required empty public constructor
    }

    public static OtherWishlistsFragment newInstance() {
        return new OtherWishlistsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_wishlist, container, false);

        wishlistRecyclerView = view.findViewById(R.id.otherWishlistRecyclerView);

        // Set up RecyclerView
        wishlist = new ArrayList<>();
        wishlistAdapter = new OtherWishListAdapter(wishlist, getArguments().getInt("other_user_id"), new OtherWishListAdapter.OtherWishlistClickedListener() {
            @Override
            public void onWishlistClicked(Wishlist wishlist) {
                OtherGiftFragment giftFragment = OtherGiftFragment.newInstance(wishlist);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, giftFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });
        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wishlistRecyclerView.setAdapter(wishlistAdapter);

        backButton = view.findViewById(R.id.backButton_otherWishlist);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Load wishlists from API
        loadWishlists();
        return view;
    }

    private void showWishlistDetails(Wishlist wishlist) {
        // Navigate to the GiftFragment and pass the wishlist ID as an argument
        GiftFragment giftFragment = GiftFragment.newInstance(wishlist);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, giftFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void loadWishlists() {
        String url = MethodsAPI.URL_BASE + "wishlists";
        String token = getArguments().getString("accessToken");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            wishlist.clear(); // Clear the existing wishlists

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject wishlistObject = response.getJSONObject(i);

                                String title = wishlistObject.getString("name");
                                String description = wishlistObject.getString("description");
                                String expiryDate = wishlistObject.getString("end_date");
                                int userId = wishlistObject.getInt("user_id");
                                int wishlistId = wishlistObject.getInt("id");
                                String creationDate = wishlistObject.getString("creation_date");

                                JSONArray giftsArray = wishlistObject.getJSONArray("gifts");
                                List<Gift> gifts = new ArrayList<>();

                                for (int j = 0; j < giftsArray.length(); j++) {
                                    JSONObject giftObject = giftsArray.getJSONObject(j);
                                    int giftId = giftObject.getInt("id");
                                    wishlistId = giftObject.getInt("wishlist_id");
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
                                }

                                if (userId == getArguments().getInt("other_user_id")) {
                                    wishlist.add(new Wishlist(title, description, expiryDate, userId, wishlistId, creationDate, gifts));
                                }
                            }

                            wishlistAdapter.notifyDataSetChanged(); // Notify the adapter about the updated data
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




