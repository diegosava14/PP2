package com.example.giftgeek.RecyclerView;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherGiftAdapter extends RecyclerView.Adapter<OtherGiftAdapter.GiftViewHolder> {

    Activity activity;
    private List<Gift> giftList;

    private OnGiftReserveListener onGiftReserveListener;
    private Context context;

    public OtherGiftAdapter(Activity activity, List<Gift> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
        this.activity = activity;
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

        private TextView giftName;
        private TextView giftDescription;
        private TextView giftPrice;
        private TextView giftPriority;

        private ImageView imageView;
        private Button reserveButton;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            giftName = itemView.findViewById(R.id.other_gift_name);
            giftDescription = itemView.findViewById(R.id.other_gift_description);
            giftPrice = itemView.findViewById(R.id.other_gift_price);
            giftPriority = itemView.findViewById(R.id.other_gift_priority);
            reserveButton = itemView.findViewById(R.id.reserveGiftButton);
            imageView = itemView.findViewById(R.id.other_gift_image_view);

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
            giftName.setText(gift.getName());
            giftDescription.setText(gift.getDescription());
            if(gift.getPrice() != null){
                giftPrice.setText(gift.getPrice().toString() + "€");
            }else{
                giftPrice.setText("No price");
            }
            giftPriority.setText(gift.getPriority());
            try{
                Picasso.with(activity).load(gift.getImageUrl()).resize(200, 200).into(imageView);
            }catch(Exception e){
                imageView.setImageResource(R.drawable.ic_baseline_card_giftcard_24);
            }
        }

    }
}