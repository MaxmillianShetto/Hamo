package com.dpsd.hamo.view.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.DonationGetterI;
import com.dpsd.hamo.databinding.FragmentCommunityRepHomeBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonorsCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.DonationGetter;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
public class CommunityRepHomeFragment extends Fragment implements DonationGetterI
{
    SupportMapFragment supportMapFragment = new SupportMapFragment();
    FusedLocationProviderClient providerClient;
    AppCompatActivity activity;
    Context context;
    ExtendedFloatingActionButton fab;
    GoogleMap gMap;
    ArrayList<DonationGetter> donations;
    private @NonNull
    FragmentCommunityRepHomeBinding binding;

    public CommunityRepHomeFragment()
    {

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

        DonorsCollection donReq = new DonorsCollection(DatabaseHandle.db);
        donReq.getRepDonations(LocalStorage.getValue("userId", getContext()), CommunityRepHomeFragment.this);

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
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            gMap = googleMap;
            if (donations != null)
            {
                for (int i = 0; i < donations.size(); i++)
                {
                    LatLng one = new LatLng(Double.parseDouble(donations.get(i).getLatitude()),
                            Double.parseDouble(donations.get(i).getLongitude()));
                    MarkerOptions options = new MarkerOptions().position(one).title(String.valueOf(donations.get(i).getName()));
                    googleMap.addMarker(options);

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(one, 10));

                }

                googleMap.setOnMarkerClickListener(marker -> {
                    String markerTitle = marker.getTitle();

                    Intent markerIntent = new Intent(getActivity(), DonationDetailsActivity.class);
                    markerIntent.putExtra("title", markerTitle);
                    startActivity(markerIntent);

                    return false;
                });
            }
        });

        providerClient = LocationServices.getFusedLocationProviderClient(getContext());
        activity = (AppCompatActivity) getActivity();
        context = getContext();

        return root;
    }

    @Override
    public void processSuccess(ArrayList<DonationGetter> donationsFromDB)
    {
        donations = donationsFromDB;
        loadMap();
    }

    @Override
    public void processFailure()
    {

    }


    private void loadMap()
    {
        for (int i = 0; i < donations.size(); i++)
        {
            LatLng one = new LatLng(Double.parseDouble(donations.get(i).getLatitude()),
                    Double.parseDouble(donations.get(i).getLongitude()));
            MarkerOptions options = new MarkerOptions().position(one).title(String.valueOf(donations.get(i).getName()));
            gMap.addMarker(options);

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(one, 10));

        }

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {

                String markerTitle = marker.getTitle();
                DonationGetter donationDetails = getRequestInfo(markerTitle);
                Intent markerIntent = new Intent(getActivity(), DonationDetailsActivity.class);
                markerIntent.putExtra("title", markerTitle);
                markerIntent.putExtra("donationDetails", donationDetails);
                startActivity(markerIntent);
                Toast.makeText(getContext(), "Clicked marker", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    public DonationGetter getRequestInfo(String summary)
    {
        for (int i = 0; i < donations.size(); i++)
        {
            if (donations.get(i).getName().equals(summary))
            {
                return donations.get(i);
            }
        }
        return null;
    }
}