package com.dpsd.hamo.dbmodel;


import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.*;
import java.util.regex.Pattern;

import com.dpsd.hamo.controllers.Login;
import com.dpsd.hamo.controllers.SignUp;
import com.dpsd.hamo.controllers.Updater;
import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.UserGetter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UsersCollection
{
    public static final String name="users";
    //fields
    public static final String fullNameField = "fullname";
    public static final String emailField ="email";
    public static final String phoneNumberField ="phonenumber";
    public static final String passwordField="password";
    public static final String roleField="role";
    public static final String stateField="state";
    public static final String gpsLocationsField ="gpslocations";
    public static final String establishmentField = "establishment";

    String TAG = "usersCollection";

    FirebaseFirestore db;

    public static String valueHolder;

    public UsersCollection(FirebaseFirestore dbhandle)
    {
        db = dbhandle;
    }

    public void addUser(String fullname, String email, String phoneNumber, String password,
                        String role, String establishment, ArrayList<GpsLocation> locations, SignUp signUp){
        try
        {
            //search if user already exist using email and phone number
            Task<QuerySnapshot> checkUserTask = db.collection(name).whereEqualTo(emailField,email).get();
            //DocumentReference documentReference =  Tasks.await(checkUserTask)

            Map<String, Object> record = new HashMap<>();
            record.put(fullNameField,fullname);
            record.put(emailField,email);
            record.put(phoneNumberField,phoneNumber);
            record.put(passwordField,password);
            record.put(roleField,role);
            record.put(stateField,"active");
            record.put(gpsLocationsField,locations);
            record.put(establishmentField,establishment);

            db.collection(name).add(record).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        signUp.proceedToHomePage(role);
                        if (!email.equals("") && !fullname.equals(""))
                        {
                            signUp.sendSignUpEmail(email, fullname);
                        }
                        else if (!phoneNumber.equals("") && !fullname.equals(""))
                        {
                            signUp.sendSignUpSms(phoneNumber,fullname);
                        }
                    }
                    else
                    {
                        signUp.showErrorMessage();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "addUser: ");
        }
    }

    public void isUser(String emailOrPhoneNumber, String password, String role, Login login){

        if(emailOrPhoneNumber.trim().equals("") || password.trim().equals(""))
        {
            login.fieldsEmptyErrorMessage();
            return;
        }

        try
        {
            Log.e(TAG, "called");

            String regexEmail = "^(.+)@(.+)$";
            String regexPhoneNumber = "^\\d{10}$";
            Pattern pattern = Pattern.compile(regexEmail);
            String field = emailField;
            if(pattern.matcher(emailOrPhoneNumber).matches())
            {
                field = emailField;
            }
            else if (pattern.compile(regexPhoneNumber).matcher(emailOrPhoneNumber).matches())
            {
                field = phoneNumberField;
            }
            else
            {
                login.invalidLoginErrorMessage();
                return;
            }
            String finalField = field;
            db.collection(name).whereEqualTo(field,emailOrPhoneNumber).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document: task.getResult())
                                {
                                    boolean emailIsValid = document.get(finalField).toString().trim().equals(emailOrPhoneNumber);
                                    boolean passwordIsValid = document.get(passwordField).toString().trim().equals(password);
                                    boolean isRole = document.get(roleField).toString().trim().equals(role);
                                    if(emailIsValid && passwordIsValid && isRole)
                                    {
                                        UserGetter  user = document.toObject(UserGetter.class);
                                        login.proceedToHomePage(document.getId(), user);
                                    }
                                    else
                                    {
                                        login.invalidLoginErrorMessage();
                                    }
                                }
                            }
                        }});

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
            //query document for user matching username, password, and Role
            Query query = usersRef.whereEqualTo(fullNameField,username)
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

    public void updateCurrentLocation(String userId,double latitude, double longitude, String locationName)
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
                                   locations.add(new GpsLocation(Double.toString(latitude), Double.toString(longitude)));
                               }
                               else if(locations.size()==1)
                               {
                                   //add current location for first time
                                   locations.add(new GpsLocation(locationName,Double.toString(latitude), Double.toString(longitude)));
                               }
                               else
                               {
                                   //update current location
                                   locations.get(1).setLatitude(Double.toString(latitude));
                                   locations.get(1).setLongitude(Double.toString(latitude));
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

    public void updateProfile(String userId, String fullname, String establishment,
                              boolean updateLocation, Updater updater)
    {
        try
        {
            if(updateLocation)
            {
                // updateCurrentLocation(userId, "", "");
            }

            Map<String, Object> newrow = new HashMap<>();
            newrow.put(fullNameField,fullname);
            newrow.put(establishmentField,establishment);
            db.collection(name).document(userId).update(newrow).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        updater.showProfileUpdateSuccess(); }
                    else
                    {
                        updater.showProfileUpdateFailure();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "updateProfile: "+ex.getMessage());
        }

    }

    public void loadProfile(String userId, EditText fullnameTbx, EditText establishmentTbx)
    {
        try
        {
            db.collection(name).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists())
                        {
                            fullnameTbx.setText(document.get(fullNameField).toString());
                            establishmentTbx.setText(document.get(establishmentField).toString());
                        }
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Log.d(TAG, "loadProfile: "+ex.getMessage());
        }
    }

}
