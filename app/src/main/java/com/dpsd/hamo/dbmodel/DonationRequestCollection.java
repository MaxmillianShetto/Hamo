package com.dpsd.hamo.dbmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dpsd.hamo.dbmodel.dbhelpers.Donor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonationRequestCollection {
    //Note : ID field is created automatically by db
    public static final String name="donationRequest";
    //fields
    public static final String summaryField="summary";
    public static final String detailsField="details";
    public static final String requestDateField="requestDate";
    public static final String stateField="state";
    public static final String representativeIdField="repId";
    public static final String donorsField="donors";

    String TAG = "DonationRequestCollection";
    FirebaseFirestore db;

    public DonationRequestCollection(FirebaseFirestore database)
    {
        db = database;
    }

    public void addDonationRequest(String summary,String details,String repId)
    {
        if(summary.trim().equals("")||details.trim().equals("")|| repId.trim().equals(""))
        {
            //show message of invalid activity
            return;
        }
        try
        {
            Map<String, Object> record = new HashMap<>();
            record.put(summaryField,summary);
            record.put(detailsField,details);
            record.put(requestDateField,"");
            record.put(stateField,"active");
            record.put(representativeIdField,repId);
            record.put(donorsField,new ArrayList<Donor>());

            db.collection(name).add(record).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {

                    }
                    else
                    {

                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonationRequest: "+ex.getMessage());
        }
    }

    public void addDonation(String donationRequestId)
    {
        if(donationRequestId.trim().equals(""))
        {
            //display error msg
            return;
        }

        try
        {
            CollectionReference donationRequestsRef = db.collection(name);
            donationRequestsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean exist = false;
                       for(QueryDocumentSnapshot document: task.getResult())
                       {
                           if(document.getId().toString().equals(donationRequestId))
                           {
                               //work with donation request
                               Map<String, Object> record = document.getData();
                               exist = true;
                               break;
                           }
                       }
                       if(!exist)
                       {
                           //request id does not exist
                       }
                    }
                    else
                    {

                    }

                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonation: "+ex.getMessage());
        }
    }
}
