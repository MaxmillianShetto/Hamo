package com.dpsd.hamo.dbmodel.dbhelpers;

public class Donor {


    private String donorId;
    private String donorName;
    private String donationDate;
    private String itemsDescription;
    private String latitude;
    private String longitude;

    public Donor(String _donorId) {
        donorId = _donorId;
    }

    public Donor(String _donorId, String _donorName) {
        this(_donorId);
        donorName = _donorName;
        donationDate = "";
    }

    public Donor(String _donorId, String _donorName, String _donationDate) {
        this(_donorId, _donorName);
        donationDate = _donationDate;
    }

    public Donor(String _donorId, String _donorName, String _donationDate,String lat,String lon) {
        this(_donorId,_donorName,_donationDate);
        latitude = lat;
        longitude = lon;
    }

    public Donor(String _donorId, String _donorName, String _donationDate,String description) {
        this(_donorId,_donorName,_donationDate);
        itemsDescription = description;
    }


    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorId() {
        return donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonationDate()
    {
        return donationDate;
    }

    public String getItemsDescription()
    {
        return itemsDescription;
    }
    public String getLatitude(){
        return  latitude;
    }
    public String getLongitude(){
        return  longitude;
    }

    @Override
    public String toString()
    {
        return "Donor{" +
                "donorId='" + donorId + '\'' +
                ", donorName='" + donorName + '\'' +
                ", donationDate='" + donationDate + '\'' +
                ", itemsDescription='" + itemsDescription + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}

