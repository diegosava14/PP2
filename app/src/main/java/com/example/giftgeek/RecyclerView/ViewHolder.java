package com.example.giftgeek.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameText;
    TextView emailText;

    RelativeLayout relativeLayout;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.user_list_image_view);
        nameText = itemView.findViewById(R.id.user_list_text_view);
        emailText = itemView.findViewById(R.id.user_email_text_view);
        relativeLayout = itemView.findViewById(R.id.relative_layout);
    }


}
