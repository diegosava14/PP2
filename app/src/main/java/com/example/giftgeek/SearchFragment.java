package com.example.giftgeek;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.RecyclerView.ItemAdapter;
import com.example.giftgeek.RecyclerView.ItemClickListener;
import com.example.giftgeek.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements ItemClickListener {

    RecyclerView recyclerView;
    SearchView searchView;
    List<User> users;
    ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        users = new ArrayList<>();
        adapter = new ItemAdapter(getActivity(), getContext(), users, this);

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getUserList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getUserList(newText);
                System.out.println(newText);
                return true;
            }
        });

        return view;
    }

    public void getUserList(String query) {
        String url = MethodsAPI.getUserByString(query);
        String accessToken = getArguments().getString("accessToken");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        users.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                users.add(User.getUserFromJson(o));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
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

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }

    @Override
    public void onItemClicked(User user) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();

        //System.out.println(user.getId());
        //System.out.println(user.getName());
        //System.out.println(user.getLastName());
        //System.out.println(user.getEmail());

        bundle.putInt("user_id", getArguments().getInt("user_id"));
        bundle.putString("accessToken", getArguments().getString("accessToken"));
        bundle.putInt("other_user_id", user.getId());
        bundle.putString("other_user_name", user.getName());
        bundle.putString("other_user_last", user.getLastName());
        bundle.putString("other_user_email", user.getEmail());
        bundle.putString("other_user_image", user.getImage());
        profileFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, profileFragment)
                .addToBackStack(null)
                .commit();
    }
}