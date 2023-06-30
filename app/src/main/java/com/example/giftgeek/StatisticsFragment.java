package com.example.giftgeek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {

    TextView numWishLists;
    TextView numWishes;
    TextView numGifts;
    ImageView backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        backButton = view.findViewById(R.id.backButton_stats);

        numWishLists = view.findViewById(R.id.num_wishlists);
        numWishes = view.findViewById(R.id.num_wishes);
        numGifts = view.findViewById(R.id.num_gifts);

        numWishLists.setText(getArguments().getString("numWishLists"));
        numWishes.setText(getArguments().getString("numWishes"));
        numGifts.setText(getArguments().getString("numGifts"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}