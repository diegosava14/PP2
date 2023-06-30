package com.example.giftgeek.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.User;
import com.example.giftgeek.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<User> users;
    private ItemClickListener listener;


    public ItemAdapter(Context context, List<User> users, ItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(users.get(position).getName() + " " + users.get(position).getLastName());
        holder.emailText.setText(users.get(position).getEmail());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(users.get(position));
            }
        });
        //holder.imageView.setImageResource(items.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
