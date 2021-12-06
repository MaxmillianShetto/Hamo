package com.dpsd.hamo.dbmodel;

import android.content.Context;
import android.location.Address;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.dpsd.hamo.controllers.ReportDisplayer;
import com.dpsd.hamo.controllers.RequestAdder;
import com.dpsd.hamo.dbmodel.dbhelpers.Donor;
import com.dpsd.hamo.dbmodel.dbhelpers.FileStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.GivingInfo;
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

    String TAG = "DonationRequestCollection";
    FirebaseFirestore db;

    public DonationRequestCollection(FirebaseFirestore database)
    {
        db = database;
    }

    public void addDonationRequest(String summary, String details, String repId,
                                   RequestAdder requestAdder)
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
            record.put(imageUriField,"");

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

    public void addDonation(String donationRequestId,String donorId,String donorName,
                            String donationDate,String itemsDescription)
    {
        if(donationRequestId.trim().equals("")||donorId.trim().equals("")||
                donorName.trim().equals("")||donationDate.trim().equals(""))
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
                                ArrayList<Donor> donors =(ArrayList<Donor>) record.get(donorsField);
                                donors.add(new Donor(donorId,donorName,donationDate,itemsDescription));
                                record.put(donorsField,donors);
                                donationRequestsRef.document(document.getId()).set(record);
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
                        //put failure or error in responding to db code here
                    }

                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "addDonation: "+ex.getMessage());
        }
    }

    public void getRequests()
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
                                repId = document.get(representativeIdField).toString();
                                try
                                {
                                    //get representative's current location
                                    Task<DocumentSnapshot> usertask = db.collection(UsersCollection.name)
                                            .document(repId).get();
                                    DocumentSnapshot userdoc = Tasks.await(usertask);
                                    ArrayList<Address> userLocation = (ArrayList<Address>) userdoc.get(UsersCollection.gpsLocationsField);
                                    if(userLocation.size()>0)
                                    {
                                        lat = Double.toString(userLocation.get(userLocation.size()-1).getLatitude());
                                        lon = Double.toString(userLocation.get(userLocation.size()-1).getLongitude());
                                    }

                                }
                                catch (Exception ex)
                                {
                                    Log.d(TAG, "onComplete: "+ex.getMessage());
                                }

                                //set all info
                                requestInfos.add(new RequestInfo(document.getId().toString(),
                                        lat,
                                        lon,
                                        document.get(summaryField).toString(),
                                        document.get(detailsField).toString(),
                                        document.get(requestDateField).toString()));
                            }
                        }

                        //call success method here

                    }
                    else
                    {
                        //put failure code here
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

    public void getDonations(String requestId,ReportDisplayer reportDisplayer)
    {
        try
        {
            db.collection(name).document(requestId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists())
                                {
                                    ArrayList<Donor> donors =(ArrayList<Donor>) document.get(donorsField);
                                    reportDisplayer.showRepresentativeReport(donors);
                                }
                            }
                            else
                            {
                                //handle db error here
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
