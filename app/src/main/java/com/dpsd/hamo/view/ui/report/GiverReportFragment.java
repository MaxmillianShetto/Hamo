package com.dpsd.hamo.view.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dpsd.hamo.controllers.DonationGetterI;
import com.dpsd.hamo.databinding.FragmentGiverReportBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.DonationRequestCollection;
import com.dpsd.hamo.dbmodel.DonorsCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.DonationGetter;
import com.dpsd.hamo.dbmodel.dbhelpers.GivingGetter;
import com.dpsd.hamo.dbmodel.dbhelpers.Givings;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;
import com.dpsd.hamo.dbmodel.dbhelpers.ReportI;
import com.dpsd.hamo.dbmodel.dbhelpers.Reporter;
import com.dpsd.hamo.dbmodel.dbhelpers.RequestSummary;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiverReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiverReportFragment extends Fragment implements DonationGetterI,
        GivingGetter, ReportI
{
    private @NonNull
    FragmentGiverReportBinding binding;

    TableLayout table;
    Spinner spinner;

    public GiverReportFragment()
    {
        // Required empty public constructor
    }

    public static GiverReportFragment newInstance(String param1, String param2)
    {
        GiverReportFragment fragment = new GiverReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentGiverReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        table = binding.reportTable;

        String role = LocalStorage.getValue("role",getContext());
        String userId = LocalStorage.getValue("userId",getContext());
        DonorsCollection donorcol = new DonorsCollection(DatabaseHandle.db);
        DonationRequestCollection dreqcol = new DonationRequestCollection(DatabaseHandle.db);

        if (role.trim().equals("giver"))
        {
            donorcol.getDonorContributions(userId, this);
        }

        else if (role.trim().equals("rep"))
        {
            donorcol.getDonations(userId, this);
        }
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void processGivingsSuccess(ArrayList<Givings> givings)
    {
        Reporter reporter = new Reporter(getContext());
        reporter.addRows(givings, table, "Givings Report");
    }

    @Override
    public void processSuccess(ArrayList<DonationGetter> donations)
    {

        Reporter donRep = new Reporter(getContext());
        donRep.addDonationsReceived(donations, table);
    }

    @Override
    public void processFailure()
    {

    }

    @Override
    public void showSummaries(ArrayList<RequestSummary> summaries)
    {
        try
        {

        }
        catch (Exception ex)
        {
            Log.d("ReportActivity", "showSummaries: " + ex.getMessage());
        }

    }
}