package com.example.giftgeek;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.giftgeek.API.MethodsAPI;
import com.example.giftgeek.Entities.Product;
import com.example.giftgeek.Entities.User;
import com.example.giftgeek.RecyclerView.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    private ImageView backButton;
    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the product list
        productList = new ArrayList<>();
        getProductList();

        // Create the adapter and pass the product list
        productAdapter = new ProductAdapter(getActivity(), productList, new ProductAdapter.ProductClickedListener() {
            @Override
            public void onProductClicked(Product product) {
                System.out.println("product clicked");
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_list, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(productAdapter);

        backButton = view.findViewById(R.id.backButton_productList);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public void getProductList(){
        String url = MethodsAPI.URL_PRODUCTS;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        productList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                productList.add(Product.getProductFromJson(o));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response Products", error.toString());
                    }
                }) {
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }
}
