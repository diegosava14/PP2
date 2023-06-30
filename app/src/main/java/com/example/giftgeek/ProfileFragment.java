package com.example.giftgeek;

import android.annotation.SuppressLint;
import android.os.Bundle;
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


public class ProfileFragment extends Fragment {


    private TextView name;
    private TextView lastName;
    private TextView email;


    private Button statsButton;
    private Button wishlistsButton;
    private Button reservedGiftsButton;

    private ImageView backButton;

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

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        wishlistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
