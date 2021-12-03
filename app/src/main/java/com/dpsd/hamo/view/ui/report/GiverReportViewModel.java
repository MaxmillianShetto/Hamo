package com.dpsd.hamo.view.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GiverReportViewModel extends ViewModel
{

    private MutableLiveData<String> mText;

    public GiverReportViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("This is giver report fragment");
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}