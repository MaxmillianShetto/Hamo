package com.dpsd.hamo.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ImageLoader {
    public ImageLoader(){

    }

    public static void LoadImage(String imageUri, ImageView imageView)
    {
        try
        {
            StorageReference imageRef = FirebaseStorage.getInstance().
                    getReference().child(imageUri);
            //create local file
            final File localfile = File.createTempFile("myimage","jpg");
            imageRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ImageLoader", "onFailure: Could not load image: "+e.getMessage());
                }
            });
        }
        catch (Exception ex)
        {
            Log.d("Main", "onCreate: "+ex.getMessage());
        }
    }
}
