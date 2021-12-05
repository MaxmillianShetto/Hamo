package com.dpsd.hamo.view.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.ShowCoordinates;
import com.dpsd.hamo.databinding.FragmentCommunityRepHomeBinding;
import com.dpsd.hamo.databinding.FragmentGiverHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityRepHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityRepHomeFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private @NonNull FragmentCommunityRepHomeBinding binding;

    SupportMapFragment supportMapFragment = new SupportMapFragment();
    FusedLocationProviderClient providerClient;
    AppCompatActivity activity;
    Context context;

    ArrayList<LatLng> locations;
    ArrayList<String> title;

    ShowCoordinates showCoordinates;

    LatLng sydney = new LatLng(-34, 151);
    LatLng bee = new LatLng(-31.08, 150.9);
    LatLng dog = new LatLng(-30, 151.34);


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityRepHomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityRepHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityRepHomeFragment newInstance(String param1, String param2)
    {
        CommunityRepHomeFragment fragment = new CommunityRepHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentCommunityRepHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.community_rep_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap)
            {
                Toast.makeText(getContext(),"Done", Toast.LENGTH_SHORT);
            }
        });

        providerClient = LocationServices.getFusedLocationProviderClient(getContext());
        activity = (AppCompatActivity) getActivity();
        context = getContext();

        locations = new ArrayList<LatLng>();
        title = new ArrayList<String>();

        locations.add(sydney);
        locations.add(bee);
        locations.add(dog);

        title.add("Sydney");
        title.add("Bee");
        title.add("Dog");

        showCoordinates = new ShowCoordinates(activity, providerClient, supportMapFragment);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //getCurrentLocation();
            showCoordinates.getCurrentLocation(activity, locations, title);
        } else
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        }

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showCoordinates.getCurrentLocation(activity, locations, title);
            }
        }
    }
}