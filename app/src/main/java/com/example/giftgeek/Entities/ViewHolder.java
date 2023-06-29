package com.example.giftgeek.Entities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.R;

import java.time.temporal.TemporalAccessor;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameText;
    TextView emailText;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.user_list_image_view);
        nameText = itemView.findViewById(R.id.user_list_text_view);
        emailText = itemView.findViewById(R.id.user_email_text_view);
    }
}
