package com.example.giftgeek.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.R;

import java.util.List;

public class ReservedGiftAdapter extends RecyclerView.Adapter<ReservedGiftAdapter.ReservedGiftViewHolder> {

    private List<Gift> reservedGifts;
    private int clickedUserId;

    public ReservedGiftAdapter(List<Gift> reservedGifts, int clickedUserId) {
        this.reservedGifts = reservedGifts;
        this.clickedUserId = clickedUserId;
    }

    @NonNull
    @Override
    public ReservedGiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserved_gift_item, parent, false);
        return new ReservedGiftViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservedGiftViewHolder holder, int position) {
        Gift gift = reservedGifts.get(position);
        holder.bind(gift);
    }

    @Override
    public int getItemCount() {
        return reservedGifts.size();
    }


    public class ReservedGiftViewHolder extends RecyclerView.ViewHolder {

        private TextView giftNameTextView;
        private TextView descriptionTextView;
        private ImageView imageView;

        public ReservedGiftViewHolder(@NonNull View itemView) {
            super(itemView);
            giftNameTextView = itemView.findViewById(R.id.reserved_gift_name);
            descriptionTextView = itemView.findViewById(R.id.reserved_gift_description);
            imageView = itemView.findViewById(R.id.reserved_gift_image);
        }

        public void bind(Gift gift) {
            giftNameTextView.setText(String.valueOf(gift.getId()));
            descriptionTextView.setText(gift.getProductUrl());
            //imageView.setText(wishlist.getEndDate());
        }
    }

}