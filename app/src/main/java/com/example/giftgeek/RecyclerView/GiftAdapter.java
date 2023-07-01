package com.example.giftgeek.RecyclerView;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.R;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    private List<Gift> giftList;
    private OnGiftDeleteListener onGiftDeleteListener;
    private OnGiftEditListener onGiftEditListener;
    private Context context;

    public GiftAdapter(List<Gift> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
    }

    public void setOnGiftDeleteListener(OnGiftDeleteListener listener) {
        this.onGiftDeleteListener = listener;
    }

    public void setOnGiftEditListener(OnGiftEditListener listener) {
        this.onGiftEditListener = listener;
    }

    public interface OnGiftDeleteListener {
        void onGiftDeleted(Gift gift);
    }

    public interface OnGiftEditListener {
        void onGiftEdited(Gift gift);

        void onGiftEditClicked(Gift gift);
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = giftList.get(position);
        holder.bind(gift);
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {
        private TextView giftUrlTextView;
        private TextView giftPriorityTextView;
        private ImageButton deleteButton;
        private ImageButton editButton;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            giftPriorityTextView = itemView.findViewById(R.id.priorityTextView);
            giftUrlTextView = itemView.findViewById(R.id.productUrlTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Find the delete button view
            editButton = itemView.findViewById(R.id.editButton); // Find the edit button view

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onGiftEditListener != null) {
                        Gift gift = giftList.get(position);
                        onGiftEditListener.onGiftEditClicked(gift);
                    }
                }
            });
        }

        public void bind(Gift gift) {
            giftUrlTextView.setText(gift.getProductUrl());
            giftPriorityTextView.setText(String.valueOf(gift.getPriority()));

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(gift);
                }
            });
        }

        private void showDeleteConfirmationDialog(Gift gift) {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this gift?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteGift(gift);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        private void deleteGift(Gift gift) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (onGiftDeleteListener != null) {
                    onGiftDeleteListener.onGiftDeleted(gift);
                }
            }
        }
    }
}