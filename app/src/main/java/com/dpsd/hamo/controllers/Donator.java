package com.dpsd.hamo.controllers;

public interface Donator
{
    public void processDonationSuccess(String donationId);

    public void processDonationFailure();
}
