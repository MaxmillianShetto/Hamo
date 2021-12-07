package com.dpsd.hamo.view.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.StorageInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controller.permissions.PermissionFactory;
import com.dpsd.hamo.controller.permissions.PermissionManager;
import com.dpsd.hamo.controller.permissions.PermissionType;
import com.dpsd.hamo.controllers.RequestAdder;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonationRequestCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.FileStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.dpsd.hamo.view.CommunityRepActivity;
import com.dpsd.hamo.view.GiverActivity;
import com.dpsd.hamo.view.login.SignUpFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateRequestActivity extends AppCompatActivity implements RequestAdder
{
    TextView uploadImageTextView;
    TextView takePhoto;
    EditText descriptionEditText;
    EditText summaryEditText;
    ImageView uploadedImageRep;
    Button submitRequest;
    Bitmap uploadedPhotoToSave;

    private LocationProvider locationProvider;

    int REQUEST_CODE_UPLOAD = 1;
    int REQUEST_CODE_CAMERA = 100;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        uploadImageTextView = findViewById(R.id.uploadImageTextView);
        takePhoto = findViewById(R.id.takePhotoTextViewRep);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        summaryEditText = findViewById(R.id.summaryEditText);
        submitRequest = findViewById(R.id.submitRequestButton);

        uploadedImageRep = findViewById(R.id.uploadedImageView);

        uploadImageTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Upload Image Clicked", Toast.LENGTH_SHORT).show();
                uploadImageFromPhone();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                captureImage();
            }
        });

        submitRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "button clicked", Toast.LENGTH_SHORT).show();
                submitDonationRequest();
            }
        });
    }

    public void submitDonationRequest(){

        String summary = summaryEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        LocationProvider provider = locationProvider;
        GpsLocation currentLocation = new GpsLocation();

        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(getApplicationContext());

        PermissionManager permissionManager = PermissionFactory
                .getPermission(PermissionType.ACCESS_FINE_LOCATION);
        DonationRequestCollection dcol = new DonationRequestCollection(DatabaseHandle.db);

        if (permissionManager.checkPermission(getApplicationContext(), this))
        {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>()
                    {
                        @Override
                        public void onSuccess(Location location)
                        {

                            dcol.addDonationRequest(summary, description,
                                    LocalStorage.getValue("userId", CreateRequestActivity.this), Double.toString(location.getLatitude()),
                                    Double.toString(location.getLongitude()), CreateRequestActivity.this);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            dcol.addDonationRequest(summary, description,
                                    LocalStorage.getValue("userId", CreateRequestActivity.this), "-1.9422403",
                                    "30.1201112", CreateRequestActivity.this);
                        }
                    });
        }

        Intent intent = new Intent(this, CommunityRepActivity.class);
        Toast.makeText(getApplicationContext(), "Your request has been saved", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    private void uploadImageFromPhone()
    {
        Intent uploadIntent = new Intent();
        uploadIntent.setType("image/*");
        uploadIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(uploadIntent, REQUEST_CODE_UPLOAD);
    }

    private void captureImage()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        Toast.makeText(getApplicationContext(), "worked to some extent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPLOAD && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            uploadedImageRep.setImageURI(imageUri);
            try
            {
                uploadedPhotoToSave = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            uploadedPhotoToSave = capturedImage;
            uploadedImageRep.setImageBitmap(capturedImage);
        }



    }

    @Override
    public void saveImage(String requestId)
    {
        if(imageUri!=null)
            FileStorage.addRequestImage(LocalStorage.getValue("userId",this),
                requestId,imageUri,this);
    }
}