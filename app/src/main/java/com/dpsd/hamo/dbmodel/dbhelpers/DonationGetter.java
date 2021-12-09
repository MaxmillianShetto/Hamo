package com.dpsd.hamo.dbmodel.dbhelpers;

import java.io.Serializable;

public class DonationGetter implements Serializable
{
    public String name;
    public String date;
    public String description;
    public String itemsImageUri;
    public String state = "active";
    public String latitude;
    public String longitude;

    public DonationGetter()
    {

    }

    public DonationGetter(String name, String date, String description, String itemsImageUri,
                          String lat, String lng)
    {
        this.name = name;
        this.date = date;
        this.description = description;
        this.itemsImageUri = itemsImageUri;
        latitude = lat;
        longitude = lng;
    }

    public String getName()
    {
        return name;
    }

    public String getDate()
    {
        return date;
    }

    public String getDescription()
    {
        return description;
    }

    public String getItemsImageUri()
    {
        return itemsImageUri;
    }

    public String getState()
    {
        return state;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }
}
