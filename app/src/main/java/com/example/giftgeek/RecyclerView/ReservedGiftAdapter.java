package com.example.giftgeek.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReservedGiftAdapter extends RecyclerView.Adapter<ReservedGiftAdapter.ReservedGiftViewHolder> {

    Activity activity;
    private List<Gift> reservedGifts;
    private int clickedUserId;

    public ReservedGiftAdapter(Activity activity, List<Gift> reservedGifts, int clickedUserId) {
        this.reservedGifts = reservedGifts;
        this.clickedUserId = clickedUserId;
        this.activity = activity;
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
            giftNameTextView.setText(gift.getName());
            descriptionTextView.setText(gift.getDescription());
            try{
                Picasso.with(activity).load(gift.getImageUrl()).resize(200, 200).into(imageView);
            }catch(Exception e){
                imageView.setImageResource(R.drawable.ic_baseline_card_giftcard_24);
            }
        }
    }

}