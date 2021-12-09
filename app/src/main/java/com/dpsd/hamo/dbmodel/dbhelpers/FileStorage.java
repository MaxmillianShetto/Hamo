package com.dpsd.hamo.dbmodel.dbhelpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonationRequestCollection;
import com.dpsd.hamo.dbmodel.DonorsCollection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FileStorage
{

    public static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    static String TAG = "FileStorage";
    static String donorsFolder = "donors";
    static String communityRepresentativeFolder = "rep";

    public static void addDonorImage(String giverId, String requestId, String donationId, Uri imageUri, Context context)
    {
        try
        {
            String pic_uri = getNextImageUri("giver", giverId, requestId, imageUri, context);
            StorageReference donorImageRef = storageRef.child(pic_uri);
            //move file to server
            donorImageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        //update donation with image uri
                        DonorsCollection dns = new DonorsCollection(DatabaseHandle.db);
                        dns.updateImageUri(donationId, pic_uri);
                    }
                    else
                    {
                        //respond to error
                    }
                }
            });

        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonorImage: " + ex.getMessage());
        }
    }

    public static void addRequestImage(String repId, String requestId, Uri imageUri,
                                       Context context)
    {
        try
        {
            String dburl = getNextImageUri("rep", repId, requestId, imageUri, context);
            StorageReference donorImageRef = storageRef.child(dburl);
            //move file to server
            donorImageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        DonationRequestCollection dcol = new DonationRequestCollection(DatabaseHandle.db);
                        dcol.updateImageUri(requestId, dburl);
                    }
                    else
                    {
                        //respond to error
                        Toast.makeText(context, "Could not save image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonorImage: " + ex.getMessage());
        }
    }

    public static String getNextImageUri(String role, String userId, String otherInfo, Uri imageUri, Context context)
    {
        String pic_id = "";
        String uri = "";
        pic_id = otherInfo + "_" + userId + "_" + System.currentTimeMillis();
        if (role.equals("rep"))
        {
            uri = communityRepresentativeFolder + "/" + pic_id;
        }
        else if (role.equals("giver"))
        {
            uri = donorsFolder + "/" + pic_id;
        }

        pic_id += "." + getFileExtension(imageUri, context);

        return pic_id;
    }


    private static String getFileExtension(Uri uri, Context context)
    {
        String ext = "";
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        ext = mime.getExtensionFromMimeType(contentResolver.getType(uri));
        return ext;
    }
}
