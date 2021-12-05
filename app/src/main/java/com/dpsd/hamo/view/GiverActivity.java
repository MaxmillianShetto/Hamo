package com.dpsd.hamo.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dpsd.hamo.R;
import com.dpsd.hamo.databinding.ActivityCommunityRepBinding;
import com.dpsd.hamo.databinding.ActivityGiverBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GiverActivity extends AppCompatActivity
{
    Boolean userLoggedIn = false;

    private ActivityGiverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityGiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");


        BottomNavigationView navView = findViewById(R.id.nav_view_giver);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_giver, R.id.navigation_report_giver, R.id.navigation_profile_giver)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_giver);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewGiver, navController);
    }

}