package com.dpsd.hamo.view.ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.DonationGetterI;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonationRequestCollection;
import com.dpsd.hamo.dbmodel.DonorsCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.DonationGetter;
import com.dpsd.hamo.dbmodel.dbhelpers.GivingGetter;
import com.dpsd.hamo.dbmodel.dbhelpers.Givings;
import com.dpsd.hamo.dbmodel.dbhelpers.ReportI;
import com.dpsd.hamo.dbmodel.dbhelpers.Reporter;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestSummary;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements DonationGetterI,
        GivingGetter, ReportI {

    TableLayout table;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        table = (TableLayout) findViewById(R.id.reportTable);

        String role ="rep";// LocalStorage.getValue("role",this);
        String userId ="NpeJ9rRlftJlESke50za"; //LocalStorage.getValue("userId",this);
        DonorsCollection donorcol = new DonorsCollection(DatabaseHandle.db);
        DonationRequestCollection dreqcol = new DonationRequestCollection(DatabaseHandle.db);

        if(role.trim().equals("giver"))
        {
           donorcol.getDonorContributions(userId,this);
        }

        else if(role.trim().equals("rep"))
        {
            donorcol.getDonations(userId,this);
        }
    }

    @Override
    public void processGivingsSuccess(ArrayList<Givings> givings) {
        Reporter reporter = new Reporter(this);
        reporter.addRows(givings,table,"Givings Report");
    }

    @Override
    public void processSuccess(ArrayList<DonationGetter> donations) {

        Reporter donRep = new Reporter(this);
        donRep.addDonationsReceived(donations,table);
    }

    @Override
    public void processFailure() {

    }

    @Override
    public void showSummaries(ArrayList<RequestSummary> summaries) {
        try
        {

        }
        catch (Exception ex)
        {
            Log.d("ReportActivity", "showSummaries: "+ex.getMessage());
        }

    }
}