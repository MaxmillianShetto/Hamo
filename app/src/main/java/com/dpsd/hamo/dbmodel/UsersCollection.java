package com.dpsd.hamo.dbmodel;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.*;

import com.dpsd.hamo.controllers.Login;
import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UsersCollection {
    public static final String name="users";
    //fields
    public static final String fullNameField = "fullname";
    public static final String usernameField="username";
    public static final String passwordField="password";
    public static final String roleField="role";
    public static final String stateField="state";
    public static final String gpsLocationsField ="gpslocations";

    String TAG = "usersCollection";

    FirebaseFirestore db;

    public static String valueHolder;

    public UsersCollection(FirebaseFirestore dbhandle)
    {
        db = dbhandle;
    }

    public void addUser(String fullname, String username, String password, String role,
                        ArrayList<GpsLocation> locations, Login login){
        try
        {
            Map<String, Object> record = new HashMap<>();
            record.put(fullNameField,fullname);
            record.put(usernameField,username);
            record.put(passwordField,password);
            record.put(roleField,role);
            record.put(stateField,"active");
            record.put(gpsLocationsField,locations);

            db.collection(name).add(record).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        //call method that takes user to the right
                    }
                    else
                    {
                        //call method that show login failure
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "addUser: ");
        }
    }

    public void isUser(String username,String password,String role,Login login){
        if(username.trim().equals("") || password.trim().equals(""))
        {
            //show error message and return
            return;
        }

        //use value holder to get user's full name
        try
        {
            db.collection(name).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document: task.getResult())
                                {
                                    if(document.get(usernameField).toString().equals(username)
                                            && document.get(passwordField).toString().equals(password) &&
                                            document.get(roleField).toString().equals(role))
                                    {
                                       // exec.processLogin();
                                        //do something with this info
                                       // String userfullname = document.get(fullNameField).toString();
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                //stay on same page and display error message
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getUser: "+ex.getMessage());
        }

    }

    public void disableUser(String username,String password, String role){
        try
        {
            CollectionReference usersRef = db.collection(name);
            //query document for user matching username, password, and role
            Query query = usersRef.whereEqualTo(usernameField,username)
                    .whereEqualTo(passwordField,password)
                    .whereEqualTo(roleField,role);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot document : task.getResult())
                        {
                            Map<String, Object> data = document.getData();
                            data.put(stateField,"inactive");
                            usersRef.document(document.getId()).set(data);
                        }
                    }
                }
            });


        }
        catch (Exception ex)
        {
            Log.d(TAG, "disableUser: "+ex.getMessage());
        }
    }

    public void updateCurrentLocation(String userId,String latitude, String longitude)
    {
        try
        {
            CollectionReference usersRef = db.collection(name);
            usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if(task.isSuccessful())
                   {
                       for(QueryDocumentSnapshot document : task.getResult())
                       {
                           if(document.getId().toString().equals(userId))
                           {
                               Map<String, Object> data = document.getData();

                               ArrayList<GpsLocation> locations = (ArrayList<GpsLocation>) document.get(gpsLocationsField);
                               if(locations.size()<=0)
                               {
                                   //add default location
                                   locations.add(new GpsLocation(latitude,longitude));
                               }
                               else if(locations.size()==1)
                               {
                                   //add current location for first time
                                   locations.add(new GpsLocation("current",latitude,longitude));
                               }
                               else
                               {
                                   //update current location
                                   locations.get(1).setLatitude(latitude);
                                   locations.get(1).setLongitude(latitude);
                               }

                               //save replace location with this location
                               data.put(gpsLocationsField,locations);
                               usersRef.document(document.getId()).set(data);
                               break;
                           }
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
            Log.d(TAG, "updateCurrentLocation: "+ex.getMessage());
        }
    }
}
