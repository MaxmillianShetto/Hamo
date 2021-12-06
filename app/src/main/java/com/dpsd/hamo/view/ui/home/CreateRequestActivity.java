package com.dpsd.hamo.view.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.StorageInfo;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.dpsd.hamo.controllers.RequestAdder;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonationRequestCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.FileStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;

import java.io.IOException;

public class CreateRequestActivity extends AppCompatActivity implements RequestAdder
{
    TextView uploadImageTextView;
    TextView takePhoto;
    EditText descriptionEditText;
    EditText summaryEditText;
    ImageView uploadedImageRep;
    Button submitRequest;
    Bitmap uploadedPhotoToSave;

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

        DonationRequestCollection dcol = new DonationRequestCollection(DatabaseHandle.db);
        dcol.addDonationRequest(summary,description,
                LocalStorage.getValue("userId",this),this);


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