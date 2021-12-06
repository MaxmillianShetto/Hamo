package com.dpsd.hamo.dbmodel.dbhelpers;

public class RequestSummary {
    public String requestId;
    public String summary;

    public RequestSummary(String id,String _summary)
    {
        requestId = id;
        summary = _summary;
    }
}
