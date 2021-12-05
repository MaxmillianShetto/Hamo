package com.dpsd.hamo.view;

import android.content.Intent;
import android.os.Bundle;

import com.dpsd.hamo.R;
import com.dpsd.hamo.databinding.ActivityCommunityRepBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class CommunityRepActivity extends AppCompatActivity
{
    Boolean userLoggedIn = false;

    private ActivityCommunityRepBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityCommunityRepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");


        BottomNavigationView navView = findViewById(R.id.nav_view_community_rep);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_community_rep, R.id.navigation_report_community_rep, R.id.navigation_profile_community_rep)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_community_rep);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewCommunityRep, navController);
    }

}