package com.dpsd.hamo.view.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controller.permissions.PermissionFactory;
import com.dpsd.hamo.controller.permissions.PermissionManager;
import com.dpsd.hamo.controller.permissions.PermissionType;

import java.util.Locale;

public class DonateActivity extends AppCompatActivity
{
    ImageView uploadedImage;

    TextView uploadImage;
    TextView takePhotoTextView;

    Button btnDonate;
    EditText description;
    private Uri imageUri;

    int REQUEST_CODE_UPLOAD = 1;
    int REQUEST_CODE_CAMERA = 100;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        uploadedImage = findViewById(R.id.uploadedImage);
        uploadImage = findViewById(R.id.uploadImage);
        takePhotoTextView = findViewById(R.id.takePhotoTextView);
        btnDonate = findViewById(R.id.btnDonate);
        description = findViewById(R.id.txtDescription);

        uploadImage.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Upload Image Clicked", Toast.LENGTH_SHORT).show();
            uploadImage();
        });

        takePhotoTextView.setOnClickListener(v -> {
            PermissionManager cameraPermission = PermissionFactory.getPermission(PermissionType.CAMERA);
            assert cameraPermission != null;
            if(cameraPermission.checkPermission(getApplicationContext(), DonateActivity.this))
            {
                captureImage();
            }
        });

        btnDonate.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Donate Button Clicked", Toast.LENGTH_SHORT).show());

        spinner = (Spinner) findViewById(R.id.langSpinner);

        final String[] languages = {"Language", "English", "French"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DonateActivity.this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String langSelected = parent.getItemAtPosition(position).toString();
                if (langSelected.equals("French"))
                {
                    setLocal(DonateActivity.this, "fr");

                    //Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    //startActivity(intent);

                }
                else if(langSelected.equals("English"))
                {
                    setLocal(DonateActivity.this, "en");
                }
                else
                    {
                        //Toast.makeText(DonateActivity.this, "Please select a language", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


    }

    private void captureImage()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        Toast.makeText(getApplicationContext(), "worked to some extent", Toast.LENGTH_SHORT).show();
    }

    public void uploadImage()
    {
        Intent uploadIntent = new Intent();
        uploadIntent.setType("image/*");
        uploadIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(uploadIntent, REQUEST_CODE_UPLOAD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPLOAD && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            uploadedImage.setImageURI(imageUri);
        }
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK)
        {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            uploadedImage.setImageBitmap(capturedImage);
        }

    }

    public void setLocal(AppCompatActivity context, String langCode)
    {
        Toast.makeText(getApplicationContext(),langCode,Toast.LENGTH_SHORT);
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }



}