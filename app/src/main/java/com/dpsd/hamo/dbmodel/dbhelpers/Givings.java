package com.dpsd.hamo.dbmodel.dbhelpers;

public class Givings
{
    public String donationDate = "";
    public String requestSummary = "";
    public String itemsDescription = "";
    public String itemsImageUri = "";

    public Givings(String donationDate, String requestSummary, String itemsDescription, String itemsImageUri)
    {
        this.donationDate = donationDate;
        this.requestSummary = requestSummary;
        this.itemsDescription = itemsDescription;
        this.itemsImageUri = itemsImageUri;
    }
}
