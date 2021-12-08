package com.dpsd.hamo.dbmodel.dbhelpers;

public class DonationGetter {
    public String name;
    public String date;
    public String description;
    public String itemsImageUri;

    public DonationGetter(){

    }

    public DonationGetter(String name, String date, String description, String itemsImageUri) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.itemsImageUri = itemsImageUri;
    }
}
