package com.example.giftgeek.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.User;
import com.example.giftgeek.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Activity activity;
    private Context context;
    private List<User> users;
    private ItemClickListener listener;


    public ItemAdapter(Activity activity, Context context, List<User> users, ItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
        this.activity = activity;
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

        try{
            Picasso.with(activity).load(users.get(position).getImage()).resize(200, 200).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
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
