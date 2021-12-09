package com.dpsd.hamo.controllers;

import com.dpsd.hamo.dbmodel.dbhelpers.RequestInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Mapper
{
    GoogleMap map;
    ArrayList<RequestInfo> requests;

    public Mapper(GoogleMap map, ArrayList<RequestInfo> requests)
    {
        this.map = map;
        this.requests = requests;
    }

    private void populateMap()
    {
        for (int i = 0; i < requests.size(); i++)
        {
            LatLng one = new LatLng(Double.parseDouble(requests.get(i).getLatitude()),
                    Double.parseDouble(requests.get(i).getLongitude()));
            MarkerOptions options = new MarkerOptions().position(one).title(String.valueOf(requests.get(i).getSummary()));
            map.addMarker(options);

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(one, 10));

        }
    }

}
