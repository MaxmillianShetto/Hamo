package com.dpsd.hamo.controllers;

import com.dpsd.hamo.dbmodel.dbhelpers.UserGetter;

public interface Login
{
    public void proceedToHomePage(String userId, UserGetter user);

    //    , String lat, String lng
    public void proceedToHomePage(String role, String userData);

    public void fieldsEmptyErrorMessage();

    public void invalidLoginErrorMessage();

    public void connectionErrorMessage();
}
