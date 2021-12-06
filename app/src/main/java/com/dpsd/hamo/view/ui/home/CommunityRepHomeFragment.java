package com.dpsd.hamo.view.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityRepHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityRepHomeFragment extends Fragment
{
    private @NonNull FragmentCommunityRepHomeBinding binding;

    SupportMapFragment supportMapFragment = new SupportMapFragment();
    FusedLocationProviderClient providerClient;
    AppCompatActivity activity;
    Context context;
    ExtendedFloatingActionButton fab;

    ArrayList<LatLng> locations;
    ArrayList<String> title;

    ShowCoordinates showCoordinates;

    LatLng sydney = new LatLng(-34, 151);
    LatLng bee = new LatLng(-31.08, 150.9);
    LatLng dog = new LatLng(-30, 151.34);

    public CommunityRepHomeFragment()
    {
        // Required empty public constructor
    }

    public static CommunityRepHomeFragment newInstance(String param1, String param2)
    {
        CommunityRepHomeFragment fragment = new CommunityRepHomeFragment();
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
        binding = FragmentCommunityRepHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        String dataSaved = LocalStorage.getValue("userId", getContext()) +
                " " + LocalStorage.getValue("role", getContext());
        Toast.makeText(getContext(),dataSaved, Toast.LENGTH_SHORT).show();

        fab = binding.extendedFab;

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent createRequestIntent = new Intent(getActivity(), CreateRequestActivity.class);
                startActivity(createRequestIntent);
            }
        });

        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.community_rep_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap)
            {
                for (int i = 0; i < locations.size(); i++)
                {
                    MarkerOptions options = new MarkerOptions().position(locations.get(i)).title(String.valueOf(title.get(i)));
                    googleMap.addMarker(options);

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations.get(i), 10));

                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker)
                    {
                        String markerTitle = marker.getTitle();

                        Intent markerIntent = new Intent(getActivity(), DonationDetailsActivity.class);
                        markerIntent.putExtra("title", markerTitle);
                        startActivity(markerIntent);

                        return false;
                    }
                });
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