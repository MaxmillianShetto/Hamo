package com.dpsd.hamo.view.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.RequestReader;
import com.dpsd.hamo.controllers.ShowCoordinates;
import com.dpsd.hamo.databinding.FragmentGiverHomeBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonationRequestCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GiverHomeFragment extends Fragment implements RequestReader
{
    private @NonNull
    FragmentGiverHomeBinding binding;


    SupportMapFragment supportMapFragment = new SupportMapFragment();
    FusedLocationProviderClient providerClient;
    AppCompatActivity activity;
    Context context;
    GoogleMap gMap;

    ArrayList<RequestInfo> requests;

    ArrayList<LatLng> locations;
    ArrayList<String> title;

    ShowCoordinates showCoordinates;

//    LatLng one = new LatLng(Double.parseDouble(requests.get(0).getLatitude()),
//            Double.parseDouble(requests.get(0).getLongitude()));

//    LatLng sydney = new LatLng(-34, 151);
//    LatLng bee = new LatLng(-31.08, 150.9);
//    LatLng dog = new LatLng(-30, 151.34);


    public GiverHomeFragment()
    {
        // Required empty public constructor
    }

    public static GiverHomeFragment newInstance(String param1, String param2)
    {
        GiverHomeFragment fragment = new GiverHomeFragment();
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
        binding = FragmentGiverHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // requests = new ArrayList<RequestInfo>();

        providerClient = LocationServices.getFusedLocationProviderClient(getContext());
        activity = (AppCompatActivity) getActivity();
        context = getContext();

        locations = new ArrayList<LatLng>();
        title = new ArrayList<String>();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.giver_map);
        DonationRequestCollection donReq = new DonationRequestCollection(DatabaseHandle.db);
        donReq.getRequests(this);
        supportMapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap)
            {
                gMap = googleMap;
                if(requests == null)
                    Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                else
                {
                    for (int i = 0; i < requests.size(); i++)
                    {
                        LatLng one = new LatLng(Double.parseDouble(requests.get(i).getLatitude()),
                                Double.parseDouble(requests.get(i).getLongitude()));
                        MarkerOptions options = new MarkerOptions().position(one).title(String.valueOf(requests.get(i).getSummary()));
                        googleMap.addMarker(options);

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(one, 10));

                    }

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                    {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker)
                        {

                            String markerTitle = marker.getTitle();

                            Intent markerIntent = new Intent(getActivity(), DonationRequestDetailsActivity.class);
                            markerIntent.putExtra("title", markerTitle);
                            startActivity(markerIntent);
                            Toast.makeText(getContext(), "Clicked marker", Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    });
                }
            }
        });
//        locations.add(sydney);
//        locations.add(bee);
//        locations.add(dog);

//        title.add("Sydney");
//        title.add("Bee");
//        title.add("Dog");

//        showCoordinates = new ShowCoordinates(getActivity(), providerClient, supportMapFragment);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //getCurrentLocation();
//            showCoordinates.getCurrentLocation(getActivity(), locations, title);
        } else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        }

        return root;
    }

    private void loadMap()
    {
        for (int i = 0; i < requests.size(); i++)
                    {
                        LatLng one = new LatLng(Double.parseDouble(requests.get(i).getLatitude()),
                                Double.parseDouble(requests.get(i).getLongitude()));
                        MarkerOptions options = new MarkerOptions().position(one).title(String.valueOf(requests.get(i).getSummary()));
                        gMap.addMarker(options);

                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(one, 10));

                    }

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                    {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker)
                        {

                            String markerTitle = marker.getTitle();

                            Intent markerIntent = new Intent(getActivity(), DonationRequestDetailsActivity.class);
                            markerIntent.putExtra("title", markerTitle);
                            startActivity(markerIntent);
                            Toast.makeText(getContext(), "Clicked marker", Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    });



    }

    @Override
    public void processRequest(ArrayList<RequestInfo> requestInfos)
    {
        requests = requestInfos;
        if (requests != null)
        {
            Toast.makeText(getContext(), requests.get(0).toString(),Toast.LENGTH_LONG).show();
        }
        loadMap();
    }

    @Override
    public void updateList(RequestInfo requestInfo)
    {
        Log.i("data", requestInfo.toString());
        requests.add(requestInfo);
    }
}