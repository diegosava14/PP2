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
import com.example.giftgeek.Entities.User;
import com.example.giftgeek.Entities.Wishlist;
import com.example.giftgeek.RecyclerView.GiftAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftFragment extends Fragment implements AddGiftDialogFragment.OnGiftAddedListener, GiftAdapter.OnGiftDeleteListener, GiftAdapter.OnGiftEditListener {
    Gift gift;
    private TextView wishlistTitleTextView;
    private RecyclerView giftRecyclerView;
    private GiftAdapter giftAdapter;
    private ImageView backButton;
    private Button addGiftButton;

    private Wishlist wishlist;
    private List<Gift> giftList;

    public static GiftFragment newInstance(Wishlist wishlist) {
        GiftFragment fragment = new GiftFragment();
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
            giftList = wishlist.getGifts(); // Assuming you have a method to get the list of gifts from the Wishlist object
        } else {
            giftList = new ArrayList<>(); // Initialize an empty list
        }
        giftAdapter = new GiftAdapter(getActivity(), giftList, requireContext());
        giftAdapter.setOnGiftDeleteListener(this);
        giftAdapter.setOnGiftEditListener(this);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        wishlistTitleTextView = view.findViewById(R.id.wishlistTitleTextView);
        giftRecyclerView = view.findViewById(R.id.giftRecyclerView);
        backButton = view.findViewById(R.id.backButton_gift);
        addGiftButton = view.findViewById(R.id.addGiftButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        addGiftButton.setOnClickListener(v -> {
            //showAddGiftDialog();
            ProductListFragment productListFragment = new ProductListFragment();
            productListFragment.setListener(this);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, productListFragment)
                    .addToBackStack(null)
                    .commit();
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

    @Override
    public void onGiftAdded(Gift gift) {
        gift.setWishlistId(wishlist.getWishlistId());
        addGift(gift);
        uploadGiftToApi(gift);
    }

    @Override
    public void onGiftDeleted(Gift gift) {
        deleteGift(gift); // Call the deleteGift method instead of removing the gift from the local list directly
    }

    @Override
    public void onGiftEdited(Gift gift) {
        editGift(gift.getId()); // Call the editGift method instead of editing the gift in the local list directly
    }

    private void addGift(Gift gift) {
        // Add the new gift to the local list and notify the adapter
        giftList.add(gift);
        giftAdapter.notifyDataSetChanged();
    }

    private void uploadGiftToApi(Gift gift) {
        String url = MethodsAPI.URL_BASE + "gifts";
        String token = getToken(); // Replace with the actual token


        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("wishlist_id", gift.getWishlistId());
            requestBody.put("priority", gift.getPriorityInt());
            requestBody.put("product_url", gift.getProductUrl());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the API response if needed
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            // Handle the error if needed
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void deleteGift(Gift gift) {
        String url = MethodsAPI.URL_BASE + "gifts/" + gift.getId();
        String token = getToken(); // Replace with the actual token

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the API response if needed
                        // Remove the gift from the local list and notify the adapter
                        giftList.remove(gift);
                        giftAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle the error if needed
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

    private void editGift(int giftId) {
        // Fetch the gift to be edited from the list
        Gift giftToEdit = getGiftById(giftId);
        for (Gift gift : giftList) {
            if (gift.getId() == giftId) {
                giftToEdit = gift;
                break;
            }
        }

        if (giftToEdit != null) {
            // Create a new instance of the AddGiftDialogFragment
            EditGiftDialogFragment dialogFragment = new EditGiftDialogFragment();

            // Set the listener to the GiftFragment
            dialogFragment.setOnGiftAddedListener(new EditGiftDialogFragment.OnGiftAddedListener() {

                @Override
                public void onGiftAdded(Gift gift) {
                    // Update the gift on the API
                    updateGift(gift, new GiftUpdateListener() {
                        @Override
                        public void onGiftUpdated() {
                            loadGifts();
                            giftAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            // Pass the existing gift's information to the dialog
            Bundle bundle = new Bundle();
            bundle.putString("productUrl", giftToEdit.getProductUrl());
            bundle.putString("priority", giftToEdit.getPriority());
            bundle.putInt("wishlistId", giftToEdit.getWishlistId());
            bundle.putInt("giftId", giftToEdit.getId());
            dialogFragment.setArguments(bundle);

            // Show the dialog
            dialogFragment.show(getChildFragmentManager(), "EditGiftDialog");
        }
    }

    private interface GiftUpdateListener {
        void onGiftUpdated();
    }

    private void updateGift(Gift gift, GiftUpdateListener giftUpdateListener) {
        String url = MethodsAPI.URL_BASE + "gifts/" + gift.getId();
        String token = getToken();

        try {
            System.out.println(gift.getId());
            System.out.println(gift.getWishlistId());
            System.out.println(gift.getProductUrl());
            System.out.println(gift.getPriorityInt());
            System.out.println(gift.isBooked());

            JSONObject requestBody = new JSONObject();
            requestBody.put("id", gift.getId());
            requestBody.put("wishlist_id", gift.getWishlistId());
            requestBody.put("product_url", gift.getProductUrl());
            requestBody.put("priority", gift.getPriorityInt());
            requestBody.put("booked", gift.isBooked());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            giftUpdateListener.onGiftUpdated(); // Notify the listener that the gift was updated
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
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            Volley.newRequestQueue(requireContext()).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Gift getGiftById(int giftId) {
        for (Gift gift : giftList) {
            if (gift.getId() == giftId) {
                return gift;
            }
        }
        return null;
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

    @Override
    public void onGiftEditClicked(Gift gift) {
        editGift(gift.getId());
    }
}