package com.dpsd.hamo.dbmodel.dbhelpers;

public class GpsLocation {
    public String locationName;
    public String latitude;
    public String longitude;

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
}
