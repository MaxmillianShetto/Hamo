package com.dpsd.hamo.dbmodel.dbhelpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FileStorage {

    static String TAG="FileStorage";
    public static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    static String donorsFolder="donors";
    static String communityRepresentativeFolder="rep";

    public static void addDonorImage(String giverId, String requestId, Uri imageUri, Context context,
                                     ProgressBar progressBar){
        try
        {
            String pic_id = requestId+"_"+giverId+"_"+System.currentTimeMillis();
            //get and append file extension
            pic_id+="."+getFileExtension(imageUri,context);
            StorageReference donorImageRef = storageRef.child(donorsFolder+"/"+pic_id);
            //move file to server
            donorImageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        //
                    }
                    else
                    {
                        //respond to error
                    }
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int progress =(int) (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            //display this on progress bar
                            progressBar.setProgress(progress);
                        }
                    });

        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonorImage: "+ex.getMessage());
        }
    }

    public static void addRequestImage(String repId,String requestId, Uri imageUri,
                                       Context context/*,ProgressBar progressBar*/){
        try
        {
            String  dburl = getNextImageUri("rep",repId,requestId,imageUri,context);
            StorageReference donorImageRef = storageRef.child(dburl);
            //move file to server
            donorImageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(context,"Image saved.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //respond to error
                        Toast.makeText(context,"Could not save image.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonorImage: "+ex.getMessage());
        }
    }

    public static String getNextImageUri(String role,String userId,String otherInfo,Uri imageUri,Context context)
    {
        String pic_id="";
        String uri ="";
        if(role.equals("rep"))
        {
            pic_id = otherInfo+"_"+userId+"_"+System.currentTimeMillis();
            uri = communityRepresentativeFolder+"/"+pic_id;
        }

        pic_id+="."+getFileExtension(imageUri,context);

        return pic_id;
    }



    private static String getFileExtension(Uri uri, Context context)
    {
        String ext = "";
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        ext = mime.getExtensionFromMimeType(contentResolver.getType(uri));
        return  ext;
    }
}
