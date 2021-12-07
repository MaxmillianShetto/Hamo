package com.dpsd.hamo.controllers;

import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;

import java.util.ArrayList;

public interface DonationGetter
{
    public void processSuccess(ArrayList<GpsLocation> gpsLocations);
    public void processFailure();
}
