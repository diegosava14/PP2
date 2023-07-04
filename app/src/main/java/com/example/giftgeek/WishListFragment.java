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
import com.example.giftgeek.RecyclerView.WishListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class WishListFragment extends Fragment implements AddWishlistDialogFragment.AddWishlistDialogListener {

    private RecyclerView wishlistRecyclerView;
    private WishListAdapter wishlistAdapter;
    private List<Wishlist> wishlist;
    public WishListFragment() {
        // Required empty public constructor
    }

    public static WishListFragment newInstance() {
        return new WishListFragment();
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

            wishlistRecyclerView = view.findViewById(R.id.wishlistRecyclerView);

            // Set up RecyclerView
            wishlist = new ArrayList<>();
            wishlistAdapter = new WishListAdapter(wishlist, getUserId(), new WishListAdapter.WishlistClickListener() {
                @Override
                public void onDeleteWishlist(int wishlistId, int position) {
                    deleteWishlist(wishlistId, position);
                }
                @Override
                public void onEditWishlist(int wishlistId) {
                    editWishlist(wishlistId);
                }

                @Override
                public void onWishlistClicked(Wishlist wishlist) {
                    showWishlistDetails(wishlist);
                }

            });
            wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            wishlistRecyclerView.setAdapter(wishlistAdapter);

            // Load wishlists from API
            loadWishlists();

            Button addButton = view.findViewById(R.id.addButton);
            addButton.setOnClickListener(v -> {
                showAddWishlistDialog();
            });

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
        String token = getToken();

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
                                String expiryDate1 = wishlistObject.getString("end_date");
                                String expiryDate2;
                                if (expiryDate1.length() > 10) {
                                    expiryDate2 = expiryDate1.substring(0, expiryDate1.indexOf("T"));
                                } else {
                                    expiryDate2 = null;
                                }
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
                                    gifts.add(gift);
                                }

                                if (userId == getUserId()) {
                                    wishlist.add(new Wishlist(title, description, expiryDate2, userId, wishlistId, creationDate, gifts));
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


    private void deleteWishlist(int wishlistId, int position) {

        for (int i = 0; i < wishlist.get(position).getGifts().size(); i++) {
            deleteWish(wishlist.get(position).getGifts().get(i));
        }

        String url = MethodsAPI.URL_BASE + "wishlists/" + wishlistId;
        String token = getToken();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Assuming the API call is successful, remove the wishlist from the RecyclerView
                        wishlist.remove(position);
                        wishlistAdapter.notifyItemRemoved(position);
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

    private void deleteWish(Gift gift) {
        String url = MethodsAPI.URL_BASE + "gifts/" + gift.getId();
        String token = getToken(); // Replace with the actual token

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
        //while (!done) {
        //}
    }

    private void editWishlist(int wishlistId) {
        // Fetch the wishlist to be edited from the list
        Wishlist wishlistToEdit = getWishlistById(wishlistId);
        for (Wishlist wishlist : wishlist) {
            if (wishlist.getWishlistId() == wishlistId) {
                wishlistToEdit = wishlist;
                break;
            }
        }

        if (wishlistToEdit != null) {
            // Create a new instance of the AddWishlistDialogFragment
            EditWishlistDialogFragment dialogFragment = new EditWishlistDialogFragment();

            // Set the listener to the WishListFragment
            Wishlist finalWishlistToEdit = wishlistToEdit;
            dialogFragment.setListener(new EditWishlistDialogFragment.EditWishlistDialogListener() {
                @Override
                public void onWishlistAdded(String title, String description, String expiryDate, int userId, String creationDate) {
                    // Update the wishlist's information
                    finalWishlistToEdit.setName(title);
                    finalWishlistToEdit.setDescription(description);
                    finalWishlistToEdit.setEndDate(expiryDate);

                    // Update the wishlist on the API
                    updateWishlist(finalWishlistToEdit, new WishlistUpdateListener() {
                        @Override
                        public void onWishlistUpdated() {
                            wishlistAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            // Pass the existing wishlist's information to the dialog
            Bundle bundle = new Bundle();
            bundle.putString("title", wishlistToEdit.getName());
            bundle.putString("description", wishlistToEdit.getDescription());
            bundle.putString("expiryDate", wishlistToEdit.getEndDate());
            dialogFragment.setArguments(bundle);

            // Show the dialog
            dialogFragment.show(getChildFragmentManager(), "EditWishlistDialog");
        }
    }

    private Wishlist getWishlistById(int wishlistId) {
        for (Wishlist wishlist : wishlist) {
            if (wishlist.getWishlistId() == wishlistId) {
                return wishlist;
            }
        }
        return null;
    }


    private void updateWishlist(Wishlist wishlist, WishlistUpdateListener listener) {
        String url = MethodsAPI.URL_BASE + "wishlists/" + wishlist.getWishlistId();
        String token = getToken(); // Replace with the actual token

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", wishlist.getWishlistId());
            requestBody.put("name", wishlist.getName());
            requestBody.put("description", wishlist.getDescription());
            requestBody.put("user_id", wishlist.getUserId());
            requestBody.put("creation_date", wishlist.getCreationDate());
            requestBody.put("end_date", wishlist.getEndDate());

            // Assuming the "gifts" field is not required for the update

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onWishlistUpdated();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private interface WishlistUpdateListener {
        void onWishlistUpdated();
    }


    private void showAddWishlistDialog() {
        AddWishlistDialogFragment dialogFragment = new AddWishlistDialogFragment();
        dialogFragment.setListener(this); // Set the listener to the WishListFragment
        dialogFragment.show(getChildFragmentManager(), "AddWishlistDialog");
    }

    @Override
    public void onWishlistAdded(String title, String description, String expiryDate, int userId, String creationDate) {
        List<Gift> gifts = new ArrayList<>();
        uploadWishlist(title, description, expiryDate, new WishlistUploadListener() {
            @Override
            public void onWishlistUploaded(int wishlistId) {
                wishlist.add(new Wishlist(title, description, expiryDate, userId, wishlistId, "date", gifts));
                wishlistAdapter.notifyDataSetChanged();
            }
        });
    }

    private interface WishlistUploadListener {
        void onWishlistUploaded(int wishlistId);
    }

    private void uploadWishlist(String title, String description, String expiryDate, WishlistUploadListener listener) {
        String url = MethodsAPI.URL_BASE + "wishlists";
        String token = getToken(); // Replace with the actual token

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", title);
            requestBody.put("description", description);
            requestBody.put("expiry_date", expiryDate);
            requestBody.put("user_id", getUserId());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int wishlistId = response.getInt("id");
                                listener.onWishlistUploaded(wishlistId);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
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




