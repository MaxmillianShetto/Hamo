package com.dpsd.hamo.controllers;

public interface Login
{
    public void proceedToHomePage(String role);
    public void fieldsEmptyErrorMessage();
    public void invalidLoginErrorMessage();

    public void connectionErrorMessage();
}
