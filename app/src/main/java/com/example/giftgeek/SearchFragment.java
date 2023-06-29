package com.example.giftgeek;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giftgeek.Entities.ItemAdapter;
import com.example.giftgeek.Entities.User;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

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
        adapter = new ItemAdapter(getContext(), users);

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search or API request here
                // You can update the list of users based on the search query
                // and update the adapter accordingly

                users.add(new User("dani"));
                users.add(new User("marti"));
                users.add(new User("marc"));
                users.add(new User("diego"));

                adapter.notifyDataSetChanged();

                getUserList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search or API request here
                // You can update the list of users based on the search query
                // and update the adapter accordingly
                return true;
            }
        });

        return view;
    }

    public void getUserList(String query) {


    }
}