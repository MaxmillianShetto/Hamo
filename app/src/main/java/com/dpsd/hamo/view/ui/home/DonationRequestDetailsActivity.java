package com.dpsd.hamo.view.ui.home;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dpsd.hamo.R;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestInfo;

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
        RequestInfo requestInfo = (RequestInfo) getIntent().getSerializableExtra("requestInfo");
        Log.d(TAG, "onCreate: n" + requestInfo.toString());
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