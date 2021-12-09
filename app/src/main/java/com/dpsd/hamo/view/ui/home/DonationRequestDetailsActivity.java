package com.dpsd.hamo.view.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dpsd.hamo.R;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestInfo;

public class DonationRequestDetailsActivity extends AppCompatActivity
{
    TextView description;
    TextView txtProceed;
    TextView txtTitle;
    ImageView viewImage;
    int REQUEST_CODE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request_details);

        txtTitle = findViewById(R.id.txtTitle);
        txtProceed = findViewById(R.id.txtProceed);
        description = findViewById(R.id.txtDesccribe);
        viewImage = findViewById(R.id.displayImage);

        String parentLink = "https://firebasestorage.googleapis.com/v0/b/hamo-98247.appspot.com/o/";
        String title = getIntent().getStringExtra("title");
        RequestInfo requestInfo = (RequestInfo) getIntent().getSerializableExtra("requestInfo");
        Log.d(TAG, "onCreate: n" + requestInfo.toString());
        txtTitle.setText(title);
        description.setText(requestInfo.getDetails());
        viewImage.setImageBitmap(BitmapFactory.decodeFile(parentLink + requestInfo.getImageUri()));


        txtProceed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent donateIntent = new Intent(DonationRequestDetailsActivity.this, DonateActivity.class);
                donateIntent.putExtra("requestId", requestInfo.getRequestId());
                donateIntent.putExtra("repId", requestInfo.getRepresentativeId());
                startActivity(donateIntent);
            }
        });
    }

}