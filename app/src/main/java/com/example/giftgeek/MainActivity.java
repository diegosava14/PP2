package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SearchFragment searchFragment;
    WishListFragment wishListFragment;
    UserFragment userFragment;

    Bundle bundle_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        searchFragment = new SearchFragment();
        wishListFragment = new WishListFragment();
        userFragment = new UserFragment();

        bundle_user = new Bundle();

        setContentView(binding.getRoot());
        replaceFragment(new WishListFragment(), "WishListFragment");

        setUserInfo();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.search:
                    Bundle bundle_search = new Bundle();
                    bundle_search.putInt("user_id", getIntent().getIntExtra("user_id", 1));
                    bundle_search.putString("accessToken", getIntent().getStringExtra("accessToken"));
                    searchFragment.setArguments(bundle_search);
                    replaceFragment(searchFragment, "SearchFragment");
                    break;
                case R.id.home:
                    replaceFragment(wishListFragment, "WishListFragment");
                    break;

                case R.id.user:

                    Log.d("user_id_MAIN", String.valueOf(getIntent().getIntExtra("user_id", 1)));

                    bundle_user.putInt("user_id", getIntent().getIntExtra("user_id", 1));
                    bundle_user.putString("accessToken", getIntent().getStringExtra("accessToken"));

                    userFragment.setArguments(bundle_user);
                    replaceFragment(userFragment, "UserFragment");
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, tag);
        transaction.addToBackStack(null); // Add the transaction to the back stack
        transaction.commit();
    }

    public void setUserInfo() {
        System.out.println(getIntent().getIntExtra("user_id", 1));
        String url = MethodsAPI.getUserById(getIntent().getIntExtra("user_id", 1));
        String accessToken = getIntent().getStringExtra("accessToken");
        System.out.println(accessToken);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            bundle_user.putString("name", response.getString("name"));
                            bundle_user.putString("lastname", response.getString("last_name"));
                            bundle_user.putString("email", response.getString("email"));
                            bundle_user.putString("image", response.getString("image"));
                            bundle_user.putString(getIntent().getStringExtra("password"), "password");
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
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}