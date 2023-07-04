package com.example.giftgeek;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.Gift;
import com.example.giftgeek.Entities.Product;
import com.example.giftgeek.RecyclerView.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AddGiftDialogFragment extends DialogFragment {

    private String giftUrl;
    private EditText giftPriorityEditText;
    private int wishlistId;
    private OnGiftAddedListener onGiftAddedListener;
    private List<Product> products;
    private List<Gift> gifts;

    public interface OnGiftAddedListener {
        void onGiftAdded(Gift gift);
    }

    public void setOnGiftAddedListener(OnGiftAddedListener listener) {
        this.onGiftAddedListener = listener;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Gift");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_gift, null);
        giftUrl = getArguments().getString("url");
        giftPriorityEditText = view.findViewById(R.id.giftPriorityEditText);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Create a new Gift object
                        Gift gift = new Gift(1, 1, giftUrl, Integer.parseInt(giftPriorityEditText.getText().toString()), false);

                        // Pass the selected gift object to the listener
                        if (onGiftAddedListener != null) {
                            onGiftAddedListener.onGiftAdded(gift);
                        }
                        getActivity().onBackPressed();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        // Initialize the products list
        products = new ArrayList<>();

        // Fetch the list of gifts from the API
        fetchGifts();

        return builder.create();
    }


    private void fetchGifts() {
        String url = MethodsAPI.URL_BASE_PRODUCTS + "/products";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        products.clear(); // Clear the existing gift names

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                products.add(Product.getProductFromJson(o));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ProductAdapter adapter = new ProductAdapter(getActivity(), products, new ProductAdapter.ProductClickedListener() {
                            @Override
                            public void onProductClicked(Product product) {

                            }
                        });

                        ListView listView = new ListView(requireContext());
                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        listView.setAdapter((ListAdapter) adapter);
                        listView.setItemChecked(0, true);

                        AlertDialog dialog = (AlertDialog) getDialog();
                        if (dialog != null) {
                            dialog.getListView().addHeaderView(listView);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wishlistId = getArguments().getInt("wishlistId");
        }
    }
}
