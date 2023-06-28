package com.example.giftgeek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.giftgeek.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SearchFragment searchFragment;
    WishListFragment wishListFragment;
    UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        searchFragment = new SearchFragment();
        wishListFragment = new WishListFragment();
        userFragment = new UserFragment();

        setContentView(binding.getRoot());
        replaceFragment(new WishListFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.search:
                    replaceFragment(searchFragment);
                    break;
                case R.id.home:
                    replaceFragment(wishListFragment);
                    break;

                case R.id.user:
                    Bundle bundle_user = new Bundle();

                    Log.d("user_id_MAIN", String.valueOf(getIntent().getIntExtra("user_id", 1)));

                    bundle_user.putInt("user_id", getIntent().getIntExtra("user_id", 1));
                    bundle_user.putString("accessToken", getIntent().getStringExtra("accessToken"));
                    userFragment.setArguments(bundle_user);
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