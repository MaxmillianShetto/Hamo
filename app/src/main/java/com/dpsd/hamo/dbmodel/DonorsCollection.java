package com.dpsd.hamo.dbmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dpsd.hamo.controllers.Donator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DonorsCollection {
    public static final String name="donations";
    public static final String donationDateField="date";
    public static final String descriptionField = "description";
    public static final String requestIdField="requestId";
    public static final String donorIdField = "donorId";
    public static final String donorNameField = "name";
    public static final String latitudeField ="latitude";
    public static final String longitudeField = "longitude";
    public static final String itemsImageUriField = "imageUri";

    FirebaseFirestore db;

    String TAG="DonorsCollection";

    public DonorsCollection(FirebaseFirestore _db)
    {
        db = _db;
    }

    public void addDonation(String donationRequestId, String donorId, String donorName,
                            String donationDate, String itemsDescription,String lat,String lon, Donator donator)
    {
        try
        {
            Map<String, Object> record = new HashMap<>();
            record.put(requestIdField, donationRequestId);
            record.put(donorIdField, donorId);
            record.put(donationDateField,donationDate);
            record.put(descriptionField,itemsDescription);
            record.put(itemsImageUriField,"");
            record.put(latitudeField,lat);
            record.put(longitudeField,lon);
            record.put(donorNameField,donorName);

            db.collection(name).add(record).addOnCompleteListener(
                    new OnCompleteListener<DocumentReference>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task)
                        {
                            if(task.isSuccessful())
                            {
                                donator.processDonationSuccess();
                            }
                            else
                            {
                                donator.processDonationFailure();
                            }
                        }
                    }
            );


        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonation: "+ex.getMessage());
        }
    }
}
