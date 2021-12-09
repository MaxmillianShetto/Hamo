package com.dpsd.hamo.controllers;

import com.dpsd.hamo.dbmodel.dbhelpers.Donor;
import com.dpsd.hamo.dbmodel.dbhelpers.GivingInfo;

import java.util.ArrayList;

public interface ReportDisplayer
{
    public void showGiverReport(ArrayList<GivingInfo> givingInfos);

    public void showRepresentativeReport(ArrayList<Donor> donors);
}
