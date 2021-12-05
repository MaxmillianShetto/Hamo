package com.dpsd.hamo.view.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dpsd.hamo.R;

public class CreateRequestActivity extends AppCompatActivity
{
    TextView uploadImageTextView;
    TextView takePhoto;
    EditText descriptionEditText;
    EditText summaryEditText;
    ImageView uploadedImageRep;
    Button submitRequest;

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
        }
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK)
        {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            uploadedImageRep.setImageBitmap(capturedImage);
        }

    }
}