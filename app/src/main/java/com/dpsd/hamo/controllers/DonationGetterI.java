package com.dpsd.hamo.controllers;

import com.dpsd.hamo.dbmodel.dbhelpers.DonationGetter;

import java.util.ArrayList;

public interface DonationGetterI
{
    public void processSuccess(ArrayList<DonationGetter> donations);

    public void processFailure();
}
