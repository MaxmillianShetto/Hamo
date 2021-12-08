package com.dpsd.hamo.dbmodel.dbhelpers;

import java.util.List;

public class UserGetter {
    public String email;
    public String role;
    public String state;
    public String establishment;
    public String password;
    public String phoneNumber;
    public String fullname;
    public List<GpsLocation> gpslocations;

    public UserGetter(String email, String role, String state, String establishment,
                      String password, String phoneNumber, String fullname, List<GpsLocation> gpslocations) {
        this.email = email;
        this.role = role;
        this.state = state;
        this.establishment = establishment;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.gpslocations = gpslocations;
    }


    public UserGetter(){

    }
}
