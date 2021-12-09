package com.dpsd.hamo.dbmodel.dbhelpers;

import java.io.Serializable;

public class RequestInfo implements Serializable
{
    String requestId;
    String representativeId;
    String latitude;
    String longitude;
    String summary;
    String details;
    String requestDate;
    String imageUri;

    public RequestInfo(String reqId, String _requestId, String lat, String lon, String _summary,
                       String _details, String reqDate, String _imageUri)
    {
        representativeId = reqId;
        latitude = lat;
        longitude = lon;
        summary = _summary;
        details = _details;
        requestDate = reqDate;
        imageUri = _imageUri;
        requestId = _requestId;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public String getSummary()
    {
        return summary;
    }

    public String getDetails()
    {
        return details;
    }

    public String getRequestDate()
    {
        return requestDate;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    public String getRepresentativeId()
    {
        return representativeId;
    }

    @Override
    public String toString()
    {
        return "RequestInfo{" +
                "requestId='" + requestId + '\'' +
                ", representativeId='" + representativeId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", summary='" + summary + '\'' +
                ", details='" + details + '\'' +
                ", requestDate='" + requestDate + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
