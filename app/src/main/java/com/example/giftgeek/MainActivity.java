package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.giftgeek.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new WishListFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.search:
                    SearchFragment searchFragment = new SearchFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("loggedInUser", getIntent().getStringExtra("loggedInUser"));
                    searchFragment.setArguments(bundle2);
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.home:
                    WishListFragment wishListFragment = new WishListFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("loggedInUser", getIntent().getStringExtra("loggedInUser"));
                    wishListFragment.setArguments(bundle1);
                    replaceFragment(wishListFragment);
                    break;

                case R.id.user:
                    UserFragment userFragment = new UserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("loggedInUser", getIntent().getStringExtra("loggedInUser"));
                    userFragment.setArguments(bundle);
                    replaceFragment(userFragment);
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}