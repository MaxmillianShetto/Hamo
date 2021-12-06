package com.dpsd.hamo.dbmodel.dbhelpers;

public class GpsLocation {
    public String locationName;
    public String latitude;
    public String longitude;

    public GpsLocation()
    {
        locationName = "Gasabo";
        latitude = "-1.9422403";
        longitude = "30.1201112";
    }

    public GpsLocation(String locName,String lat, String lon)
    {
        locationName = locName;
        latitude = lat;
        longitude = lon;
    }
    public GpsLocation(String lat, String lon)
    {
        locationName = "default";
        latitude = lat;
        longitude = lon;
    }



    public void setLatitude(String newLatitude)
    {
        latitude = newLatitude;
    }
    public void setLongitude(String newLongitude)
    {
        longitude = newLongitude;
    }
    public void setLocationName(String newLocation)
    {
        locationName = newLocation;
    }
}
