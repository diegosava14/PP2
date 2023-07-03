package com.example.giftgeek.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Wishlist;
import com.example.giftgeek.R;

import java.util.List;

public class OtherWishListAdapter extends RecyclerView.Adapter<OtherWishListAdapter.WishlistViewHolder> {

    private List<Wishlist> wishlistItems;
    private int clickedUserId;
    private OtherWishlistClickedListener wishlistClickListener;

    public OtherWishListAdapter(List<Wishlist> wishlistItems, int clickedUserId, OtherWishlistClickedListener wishlistClickListener) {
        this.wishlistItems = wishlistItems;
        this.clickedUserId = clickedUserId;
        this.wishlistClickListener = wishlistClickListener;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_wishlist_item, parent, false);
        return new WishlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Wishlist wishlistItem = wishlistItems.get(position);
        holder.bind(wishlistItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Wishlist wishlist = wishlistItems.get(position);
                    wishlistClickListener.onWishlistClicked(wishlist);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public interface OtherWishlistClickedListener {
        void onWishlistClicked(Wishlist wishlist);
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView expiryDateTextView;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.otherNameTextView);
            descriptionTextView = itemView.findViewById(R.id.otherDescriptionTextView);
            expiryDateTextView = itemView.findViewById(R.id.otherExpiryDateTextView);
        }

        public void bind(Wishlist wishlist) {
            nameTextView.setText(wishlist.getName());
            descriptionTextView.setText(wishlist.getDescription());
            expiryDateTextView.setText(wishlist.getEndDate());
        }
    }

    public void setClickedUserId(int clickedUserId) {
        this.clickedUserId = clickedUserId;
        notifyDataSetChanged();
    }

    public void setWishlistItems(List<Wishlist> wishlistItems) {
        this.wishlistItems = wishlistItems;
        notifyDataSetChanged();
    }
}