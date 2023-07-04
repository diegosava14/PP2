package com.example.giftgeek.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftgeek.Entities.Product;
import com.example.giftgeek.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Activity activity;
    private List<Product> productList;

    public ProductAdapter(Activity activity, List<Product> productList) {
        this.productList = productList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Retrieve the product at the given position
        Product product = productList.get(position);

        // Bind the product data to the ViewHolder
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView productDescriptionTextView;
        private TextView productPriceTextView;

        private ImageView productImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.user_list_text_view);
            productDescriptionTextView = itemView.findViewById(R.id.description);
            productPriceTextView = itemView.findViewById(R.id.price);
            productImageView = itemView.findViewById(R.id.user_list_image_view);
        }

        public void bind(Product product) {
            // Bind the product data to the views
            productNameTextView.setText(product.getName());
            productDescriptionTextView.setText(product.getDescription());
            productPriceTextView.setText(String.valueOf(product.getPrice())+" €");

            try{
                Picasso.with(activity).load(product.getImageUrl()).resize(200, 200).into(productImageView);
            }catch(Exception e){
                productImageView.setImageResource(R.drawable.ic_baseline_card_giftcard_24);
            }
        }
    }
}
