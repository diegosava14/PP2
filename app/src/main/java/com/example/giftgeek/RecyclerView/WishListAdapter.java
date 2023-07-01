package com.example.giftgeek.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Wishlist;
import com.example.giftgeek.R;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishlistViewHolder> {

    private List<Wishlist> wishlistItems;
    private int loggedInUserId;
    private WishlistClickListener wishlistClickListener;

    public WishListAdapter(List<Wishlist> wishlistItems, int loggedInUserId, WishlistClickListener wishlistClickListener) {
        this.wishlistItems = wishlistItems;
        this.loggedInUserId = loggedInUserId;
        this.wishlistClickListener = wishlistClickListener;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new WishlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Wishlist wishlistItem = wishlistItems.get(position);
        holder.bind(wishlistItem);

        if (wishlistItem.getUserId() == loggedInUserId) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Wishlist wishlist = wishlistItems.get(position);
                        showDeleteConfirmationDialog(v.getContext(), wishlist.getWishlistId(), position);
                    }
                }
            });
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Wishlist wishlist = wishlistItems.get(position);
                    wishlistClickListener.onEditWishlist(wishlist.getWishlistId());
                }
            }
        });

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

    private void showDeleteConfirmationDialog(Context context, int wishlistId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Wishlist");
        builder.setMessage("Are you sure you want to delete this wishlist?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wishlistClickListener.onDeleteWishlist(wishlistId, position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public interface WishlistClickListener {
        void onDeleteWishlist(int wishlistId, int position);

        void onEditWishlist(int wishlistId);

        void onWishlistClicked(Wishlist wishlist);
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView expiryDateTextView;
        private ImageButton editButton;
        private ImageButton deleteButton;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Wishlist wishlist) {
            nameTextView.setText(wishlist.getName());
            descriptionTextView.setText(wishlist.getDescription());
            expiryDateTextView.setText(wishlist.getEndDate());
        }
    }

    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        notifyDataSetChanged();
    }

    public void setWishlistItems(List<Wishlist> wishlistItems) {
        this.wishlistItems = wishlistItems;
        notifyDataSetChanged();
    }
}