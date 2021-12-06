package com.dpsd.hamo.view.login;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controller.permissions.PermissionFactory;
import com.dpsd.hamo.controller.permissions.PermissionManager;
import com.dpsd.hamo.controller.permissions.PermissionType;
import com.dpsd.hamo.controllers.SignUp;
import com.dpsd.hamo.databinding.FragmentSignUpBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.UsersCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;
import com.dpsd.hamo.view.UserActivityFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements SignUp, AdapterView.OnItemSelectedListener
{
    private FragmentSignUpBinding binding;
    private LocationProvider locationProvider;

    private String role = "";
    private Map<String, String> roleMap;

    public TextView loginTransitionTextView;
    public EditText emailOrPhoneNumberEditText;
    public EditText passwordEditText;
    public EditText confirmPasswordEditText;
    public EditText fullNameEditText;
    public Button singUpButton;
    public Spinner roleSpinner;


    public SignUpFragment()
    {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2)
    {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginTransitionTextView = binding.loginTransitionTextView;

        emailOrPhoneNumberEditText = binding.emailOrPhoneNumberEditTextSignUp;
        passwordEditText = binding.passwordEditTextSignUp;
        confirmPasswordEditText = binding.confirmPasswordEditText;
        fullNameEditText = binding.fullNameEditText;

        singUpButton = binding.signUpButton;

        roleSpinner = binding.roleSpinner;
        roleSpinner.setOnItemSelectedListener(this);

        loginTransitionTextView.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                loadLoginPage(v);
            }
        });

        singUpButton.setOnClickListener(v -> {
            UsersCollection collection = new UsersCollection(DatabaseHandle.db);
            String fullName = fullNameEditText.getText().toString();
            String userEmailOrPhoneNumber = emailOrPhoneNumberEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if(role.trim().equals(""))
            {
                role = roleMap.get(roleSpinner.getSelectedItem().toString());
            }

            String regexEmail = "^(.+)@(.+)$";
            String regexPhoneNumber = "^\\d{10}$";
            Pattern pattern = Pattern.compile(regexEmail);
            String email = "";
            String phoneNumber = "";
            if(pattern.matcher(userEmailOrPhoneNumber).matches())
            {
                email = userEmailOrPhoneNumber;
            }
            else if (pattern.compile(regexPhoneNumber).matcher(userEmailOrPhoneNumber).matches())
            {
                phoneNumber = userEmailOrPhoneNumber;
            }

            PermissionManager permissionManager = PermissionFactory.getPermission((PermissionType.ACCESS_FINE_LOCATION));
            if (permissionManager.checkPermission(getContext(), getActivity()))
            {
                saveUserDetails(fullName, email, phoneNumber, password,role,"",
                         SignUpFragment.this, collection);
            }

        });

        roleMap = new HashMap<String, String>();
        roleMap.put(getString(R.string.giver), getString(R.string.roleGiver));
        roleMap.put(getString(R.string.community_rep), getString(R.string.roleRep));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_array , android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        return root;
    }

    public void loadLoginPage(View view)
    {
        Fragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_login, loginFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        role = roleMap.get(roleSpinner.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void proceedToHomePage(String role)
    {
        Intent intent = new Intent(getActivity(), (Class<?>) UserActivityFactory.loadActivity(role));
        startActivity(intent);
    }

    @Override
    public void showErrorMessage()
    {
        Toast.makeText(getContext(), "Sign up unsuccessful, please try again later", Toast.LENGTH_SHORT);
    }

    public void saveUserDetails(String fullName, String email, String phoneNumber, String password,
                                String role, String establishment, SignUpFragment signUpFragment,
                                UsersCollection collection)
    {
        Toast.makeText(getContext(), "Getting location details", Toast.LENGTH_SHORT).show();

        LocationProvider provider = locationProvider;
        GpsLocation currentLocation = new GpsLocation();

        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(getContext());

        PermissionManager permissionManager = PermissionFactory
                .getPermission(PermissionType.ACCESS_FINE_LOCATION);

        if (permissionManager.checkPermission(getContext(), getActivity()))
        {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location)
                {
                    System.out.println(location);
                    if (location != null)
                    {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addressList = null;

                        try
                        {
                            addressList = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);
                            if (addressList != null & addressList.size() > 0)
                            {
                                Address currentAddress = addressList.get(0);

                                collection.addUser(fullName, email, phoneNumber, password,role,"",
                                        getLocation(currentAddress, currentLocation), SignUpFragment.this);
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    }


                }
            })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addressList = null;

                            try
                            {
                                addressList = geocoder.getFromLocation(
                                        -1.9422403,30.1201112, 1);

                                collection.addUser(fullName, email, phoneNumber, password,role,"",
                                        getLocation(addressList.get(0), currentLocation), SignUpFragment.this);
                            }
                            catch (IOException er)
                            {
                                er.printStackTrace();
                            }
                            Log.i("Location", addressList.get(0).toString());



                        }
                    });
        }
    }

    public ArrayList<GpsLocation> getLocation(Address currentAdd, GpsLocation currentLoc)
    {
        StringBuilder locationDetails = new StringBuilder();
        locationDetails.append("Area: " +currentAdd.getSubAdminArea());
        String moreDetails = currentAdd.getThoroughfare();
        if (!moreDetails.contains("Unnamed"))
        {
            locationDetails.append(" Details:" + moreDetails);
        }
        currentLoc.setLocationName(locationDetails.toString());
        currentLoc.setLatitude(Double.toString(currentAdd.getLatitude()));
        currentLoc.setLongitude(Double.toString(currentAdd.getLongitude()));
        ArrayList<GpsLocation> locations = new ArrayList<GpsLocation>();
        locations.add(currentLoc);
        return locations;
    }
}