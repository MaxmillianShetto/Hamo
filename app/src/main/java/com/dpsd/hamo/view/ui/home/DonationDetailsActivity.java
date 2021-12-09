package com.dpsd.hamo.view.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controller.permissions.PermissionFactory;
import com.dpsd.hamo.controller.permissions.PermissionManager;
import com.dpsd.hamo.controller.permissions.PermissionType;
import com.dpsd.hamo.controllers.ImageLoader;
import com.dpsd.hamo.dbmodel.dbhelpers.DonationGetter;
import com.dpsd.hamo.view.CommunityRepActivity;

public class DonationDetailsActivity extends AppCompatActivity
{
    TextView donorName;
    TextView donationDetails;
    TextView acceptDonation;
    TextView declineDonation;
    ImageView imageSentByDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);

        DonationGetter donation = (DonationGetter) getIntent().getSerializableExtra("donationDetails");

        donorName = findViewById(R.id.donorNameTextView);
        donationDetails = findViewById(R.id.donationDetailsTextView);
        acceptDonation = findViewById(R.id.acceptTextView);
        declineDonation = findViewById(R.id.declineTextView);
        imageSentByDonor = findViewById(R.id.imageSentByDonor);

        donorName.setText(donation.getName());
        donationDetails.setText(donation.getDescription());

        if (donation.getItemsImageUri() != null)
        {
            ImageLoader.LoadImage(donation.getItemsImageUri(),imageSentByDonor);
        }

        acceptDonation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "You may go to donors location to get items", Toast.LENGTH_LONG).show();
                PermissionManager permissionManager = PermissionFactory.getPermission(PermissionType.ACCESS_FINE_LOCATION);
                if(permissionManager.checkPermission(getApplicationContext(), DonationDetailsActivity.this))
                {
//                    startUpdateRepLocationService();
                }
                loadHome();
            }
        });

        declineDonation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadHome();
            }
        });
    }

    void loadHome()
    {
        Intent homeIntent = new Intent(DonationDetailsActivity.this, CommunityRepActivity.class);
        startActivity(homeIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager permissionManager = PermissionFactory.getPermission(PermissionType.ACCESS_FINE_LOCATION);
        if(permissionManager.checkPermission(getApplicationContext(), DonationDetailsActivity.this))
        {
//                    startUpdateRepLocationService();
        }
    }
}