package com.dpsd.hamo.view.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunityRepReportViewModel extends ViewModel
{

    private MutableLiveData<String> mText;

    public CommunityRepReportViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("This is community rep report fragment");
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}