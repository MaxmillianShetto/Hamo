package com.dpsd.hamo.dbmodel;

import android.content.Context;
import android.location.Address;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dpsd.hamo.controllers.DonationGetter;
import com.dpsd.hamo.controllers.Donator;
import com.dpsd.hamo.controllers.ReportDisplayer;
import com.dpsd.hamo.controllers.RequestAdder;
import com.dpsd.hamo.controllers.RequestReader;
import com.dpsd.hamo.dbmodel.dbhelpers.Donor;
import com.dpsd.hamo.dbmodel.dbhelpers.FileStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.GivingInfo;
import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestInfo;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestSummary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public static final String imageUriField ="imageuri";
    public static final String latitudeField="latitude";
    public static final String longitudeField = "longitude";
    public static final String donationRequestIdField="donationRequestId";
    public static final String donorIdField = "donorId";


    String TAG = "DonationRequestCollection";
    FirebaseFirestore db;

    public DonationRequestCollection(FirebaseFirestore database)
    {
        db = database;
    }

    public void addDonationRequest(String summary, String details, String repId,String lat,String lon,
                                   RequestAdder requestAdder)
    {
        if(summary.trim().equals("")||details.trim().equals("")|| repId.trim().equals(""))
        {
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
            record.put(imageUriField,"");
            record.put(latitudeField,lat);
            record.put(longitudeField,lon);

            db.collection(name).add(record).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                       DocumentReference doc = task.getResult();
                       requestAdder.saveImage(doc.getId());
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

    public void updateImageUri(String requestId,String dburi)
    {
        try
        {
            db.collection(name).document(requestId).update(imageUriField,dburi).addOnCompleteListener(
                    new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                                Log.d(TAG, "onComplete: db uri updated");
                        }
                    }
            );
        }
        catch (Exception ex)
        {
            Log.d(TAG, "updateImageUri: "+ex.getMessage());
        }
    }


    public void getRequests(RequestReader requestReader)
    {
        try
        {
            CollectionReference donRequestRef = db.collection(name);
            donRequestRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                         ArrayList<RequestInfo> requestInfos = new ArrayList<>();
                        String repId = "",lat="", lon="";
                        for(QueryDocumentSnapshot document: task.getResult())
                        {
                            if(document.get(stateField).toString().trim().equals("active"))
                            {
                                //get current location
                                requestInfos.add(new RequestInfo(document.get(representativeIdField).toString(),
                                        document.get(latitudeField).toString(),
                                        document.get(longitudeField).toString(),
                                        document.get(summaryField).toString(),
                                        document.get(detailsField).toString(),
                                        document.get(requestDateField).toString(),
                                        document.get(imageUriField).toString()
                                        ));

                            }

                        }

                        //call success method here
                        requestReader.processRequest(requestInfos);

                    }
                    else
                    {
                        //put failure code here
                        Log.d(TAG, "onComplete: error");
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getRequests: "+ex.getMessage());
        }
    }

    public void populateRequestIdsAndSummaries(Spinner spinner, Context context)
    {
        try
        {
            db.collection(name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        ArrayList<RequestSummary> summaries = new ArrayList<>();
                        for (QueryDocumentSnapshot document: task.getResult())
                        {
                            summaries.add(new RequestSummary(document.getId().toString(),
                                    document.get(summaryField).toString()));
                        }

                        ArrayAdapter<RequestSummary> adapter = new ArrayAdapter<RequestSummary>(context, android.R.layout.simple_spinner_dropdown_item, summaries);
                        spinner.setAdapter(adapter);
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getRequestIdsAndSummaries: "+ex.getMessage());
        }
    }

    public void getContributions(String giverId, ReportDisplayer reportDisplayer)
    {
        //get representative name, summary, and donation date
        try
        {
            db.collection(name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        ArrayList<Donor> donors;
                        ArrayList<GivingInfo> givingInfos = new ArrayList<>();
                        for(QueryDocumentSnapshot document: task.getResult())
                        {
                            donors = (ArrayList<Donor>) document.get(donorsField);
                            for (Donor d : donors)
                            {
                                if(d.getDonorId().equals(giverId))
                                {
                                    //get rep name using his id
                                    String repName = "";
                                    try
                                    {
                                        Task<DocumentSnapshot> usertask = db.collection(UsersCollection.name).document(document.get(representativeIdField).toString()).get();
                                        DocumentSnapshot userdoc = Tasks.await(usertask);
                                        repName = userdoc.get(UsersCollection.fullNameField).toString();
                                    }
                                    catch (Exception ex)
                                    {
                                        Log.d(TAG, "onComplete: "+ex.getMessage());
                                    }

                                    givingInfos.add( new GivingInfo(
                                            repName,
                                            document.get(summaryField).toString(),
                                            document.get(detailsField).toString()));
                                    break;
                                }
                            }
                        }

                        //work with the list of giving info
                        reportDisplayer.showGiverReport(givingInfos);
                    }
                    else
                    {
                        //failure info
                    }

                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getContributions: "+ex.getMessage());
        }
    }

    public void getDonations(String requestId, DonationGetter donationGetter)
    {
        try
        {
            db.collection("donations").whereEqualTo(donationRequestIdField,requestId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                ArrayList<String> donorList = new ArrayList<>();
                                for(DocumentSnapshot doc : task.getResult())
                                {
                                    donorList.add(doc.get(donorIdField).toString());
                                }
                                if(donorList.size()>0)
                                {
                                    db.collection(UsersCollection.name).whereIn("id",donorList)
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                ArrayList<GpsLocation> locations = new ArrayList<>();
                                                GpsLocation loc;
                                                for (DocumentSnapshot udoc : task.getResult())
                                                {
                                                    loc = (GpsLocation) udoc.get(UsersCollection.gpsLocationsField);
                                                    locations.add(loc);
                                                }

                                                //respond to success
                                                donationGetter.processSuccess(locations);
                                            }
                                            else
                                            {
                                                //failure
                                                donationGetter.processFailure();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

        }
        catch (Exception ex)
        {
            Log.d(TAG, "getDonationsPerRequest: "+ex.getMessage());
        }
    }
}
