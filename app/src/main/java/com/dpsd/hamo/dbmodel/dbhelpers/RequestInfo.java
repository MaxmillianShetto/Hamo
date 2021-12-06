package com.dpsd.hamo.dbmodel.dbhelpers;

public class RequestInfo {
    String requestId;
    String latitude;
    String longitude;
    String summary;
    String details;
    String requestDate;

    public RequestInfo(String reqId,String lat, String lon,String _summary,String _details,String reqDate)
    {
        requestId = reqId;
        latitude = lat;
        longitude = lon;
        summary = _summary;
        details = _details;
        requestDate = reqDate;
    }

    public String getLatitude()
    {
        return  latitude;
    }
    public String getLongitude()
    {
        return longitude;
    }
    public String getRequestId()
    {
        return  requestId;
    }
    public String getSummary()
    {
        return summary;
    }
    public String getDetails()
    {
        return  details;
    }
    public String getRequestDate()
    {
        return  requestDate;
    }
}
