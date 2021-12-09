package com.dpsd.hamo.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dpsd.hamo.R;
import com.dpsd.hamo.databinding.ActivityAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity
{
    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");

        BottomNavigationView navView = findViewById(R.id.nav_view_admin);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_admin, R.id.navigation_report_admin, R.id.navigation_profile_admin)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewAdmin, navController);
    }
}