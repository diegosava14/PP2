package com.example.giftgeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;

public class UserFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;

    private String loggedInUser;
    private TextView email;
    private TextView username;

    private Button logoutButton;
    private Button statsButton;
    private ImageView editProfileButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            loggedInUser = arguments.getString("loggedInUser");
        }

        sharedPreferences = getActivity().getSharedPreferences("UserCredentials", getActivity().MODE_PRIVATE);
        sharedPreferences2 = getActivity().getSharedPreferences("Usernames", getActivity().MODE_PRIVATE);

        email = view.findViewById(R.id.email_textview);
        username = view.findViewById(R.id.username_textview);

        logoutButton = view.findViewById(R.id.logoutButton);
        statsButton = view.findViewById(R.id.statsButton);
        editProfileButton = view.findViewById(R.id.editButton);

        email.setText(loggedInUser);
        username.setText(sharedPreferences2.getString(loggedInUser, "Username"));

        return view;
    }
}
