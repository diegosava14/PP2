package com.example.giftgeek.RecyclerView;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.R;

import java.util.List;

public class OtherGiftAdapter extends RecyclerView.Adapter<OtherGiftAdapter.GiftViewHolder> {

    private List<Gift> giftList;

    private OnGiftReserveListener onGiftReserveListener;
    private Context context;

    public OtherGiftAdapter(List<Gift> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
    }

    public void setOnGiftReserveListener(OnGiftReserveListener listener) {
        this.onGiftReserveListener = listener;
    }

    public interface OnGiftReserveListener {
        void onGiftReserved(Gift gift);
    }


    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_gift_item, parent, false);
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
        private Button reserveButton;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            giftPriorityTextView = itemView.findViewById(R.id.otherPriorityTextView);
            giftUrlTextView = itemView.findViewById(R.id.otherProductUrlTextView);
            reserveButton = itemView.findViewById(R.id.reserveGiftButton);

            reserveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onGiftReserveListener != null) {
                        Gift gift = giftList.get(position);
                        onGiftReserveListener.onGiftReserved(gift);
                    }
                }
            });
        }

        public void bind(Gift gift) {
            giftUrlTextView.setText(gift.getProductUrl());
            giftPriorityTextView.setText(String.valueOf(gift.getPriority()));
        }

    }
}