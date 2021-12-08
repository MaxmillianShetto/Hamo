package com.dpsd.hamo.dbmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dpsd.hamo.controllers.DonationGetterI;
import com.dpsd.hamo.controllers.Donator;
import com.dpsd.hamo.dbmodel.dbhelpers.DonationGetter;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
    public static final String stateField="state";
    public static final String representiveIdField="repId";

    FirebaseFirestore db;

    String TAG="DonorsCollection";

    public DonorsCollection(FirebaseFirestore _db)
    {
        db = _db;
    }

    public void addDonation(String donationRequestId, String donorId, String donorName,
                            String donationDate, String itemsDescription,String lat,String lon,
                            String repId, Donator donator)
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
            record.put(stateField,"pending");
            record.put(representiveIdField,repId);

            db.collection(name).add(record).addOnCompleteListener(
                    new OnCompleteListener<DocumentReference>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task)
                        {
                            if(task.isSuccessful())
                            {
                                donator.processDonationSuccess(task.getResult().getId().toString());
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

    public void updateImageUri(String donationId, String imageUri)
    {
        try
        {
           db.collection(name).document(donationId).update(itemsImageUriField,imageUri)
           .addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful())
                     {
                         Log.d(TAG, "onComplete: image saved");
                     }
                     else
                         Log.d(TAG, "onComplete: failed to save image.");
               }
           }) ;
        }
        catch (Exception ex)
        {
            Log.d(TAG, "updateImageUri: "+ex.getMessage());
        }
    }

    public void getRepDonations(String repId,DonationGetterI donationProcessor)
    {
        try
        {
            db.collection(name).whereEqualTo(representiveIdField,repId).whereEqualTo(stateField,"pending").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                ArrayList<DonationGetter> donations = new ArrayList<>();
                                for (DocumentSnapshot doc : task.getResult())
                                {
                                    donations.add(new DonationGetter(doc.get(donorNameField).toString(),
                                            doc.get(donationDateField).toString(),doc.get(descriptionField).toString(),
                                            doc.get(itemsImageUriField).toString(),doc.get(latitudeField).toString(),
                                            doc.get(longitudeField).toString()));
                                }

                                donationProcessor.processSuccess(donations);

                            }
                            else
                            {
                                donationProcessor.processFailure();
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getDonations: "+ex.getMessage());
        }
    }
    public void getDonations(String requestId, DonationGetterI donationProcessor)
    {
        try
        {
           db.collection(name).whereEqualTo(requestIdField,requestId).get()
           .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if(task.isSuccessful())
                   {
                       ArrayList<DonationGetter> donations = new ArrayList<>();
                       for (DocumentSnapshot doc : task.getResult())
                       {
                           donations.add(new DonationGetter(doc.get(donorNameField).toString(),
                                   doc.get(donationDateField).toString(),doc.get(descriptionField).toString(),
                                   doc.get(itemsImageUriField).toString(),doc.get(latitudeField).toString(),
                                   doc.get(longitudeField).toString()));
                       }

                       donationProcessor.processSuccess(donations);

                   }
                   else
                   {
                       donationProcessor.processFailure();
                   }
               }
           });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getDonations: "+ex.getMessage());
        }
    }
}
