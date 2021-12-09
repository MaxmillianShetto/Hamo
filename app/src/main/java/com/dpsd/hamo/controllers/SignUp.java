package com.dpsd.hamo.controllers;

public interface SignUp
{
    public void proceedToHomePage(String role);

    public void showErrorMessage();

    public void sendSignUpEmail(String recipientEmail, String name);

    public void sendSignUpSms(String phoneNumber, String name);

}
