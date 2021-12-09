package com.dpsd.hamo.controllers;

import com.dpsd.hamo.dbmodel.dbhelpers.RequestInfo;

import java.util.ArrayList;

public interface RequestReader
{
    public void processRequest(ArrayList<RequestInfo> requestInfos);

    public void updateList(RequestInfo requestInfo);

}
