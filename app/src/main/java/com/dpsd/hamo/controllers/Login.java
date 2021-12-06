package com.dpsd.hamo.controllers;

public interface Login
{
    public void proceedToHomePage(String role);
    public void proceedToHomePage(String role, String userData);
    public void fieldsEmptyErrorMessage();
    public void invalidLoginErrorMessage();

    public void connectionErrorMessage();
}
