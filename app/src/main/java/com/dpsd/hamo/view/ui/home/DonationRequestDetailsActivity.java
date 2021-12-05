package com.dpsd.hamo.view.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpsd.hamo.R;

public class DonationRequestDetailsActivity extends AppCompatActivity
{
    TextView uploadImage;
    TextView txtProceed;
    TextView txtTitle;
    ImageView imageView;
    int REQUEST_CODE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request_details);

        txtTitle = findViewById(R.id.txtTitle);
        txtProceed = findViewById(R.id.txtProceed);

        String title = getIntent().getStringExtra("title");
        txtTitle.setText(title);

        txtProceed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent donateIntent = new Intent(DonationRequestDetailsActivity.this, DonateActivity.class);
                startActivity(donateIntent);
            }
        });
    }

}