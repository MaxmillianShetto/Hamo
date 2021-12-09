package com.dpsd.hamo.dbmodel.dbhelpers;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Reporter
{

    Context _context;
    TextView textView;

    public Reporter(Context context)
    {
        _context = context;
    }

    TableRow getHeaderRow(String headerText)
    {
        TableRow hrow = new TableRow(_context);
        hrow.setBackgroundColor(Color.parseColor("#000000"));
        textView = new TextView(_context);
        textView.setText(headerText);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setHeight(60);
        // set params here
        TableRow.LayoutParams hparams = new TableRow.LayoutParams();
        hparams.setMargins(10, 10, 10, 10);
        hrow.setLayoutParams(hparams);
        textView.setPadding(10, 10, 10, 10);
        hrow.addView(textView);
        return hrow;
    }

    TableRow getTitleRow(String tableTitle)
    {
        TableRow hrow = new TableRow(_context);
        hrow.setBackgroundColor(Color.parseColor("#4169E1"));

        textView = new TextView(_context);
        textView.setText(tableTitle);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setHeight(60);
        // set params here
        TableRow.LayoutParams hparams = new TableRow.LayoutParams();
        hparams.setMargins(10, 10, 10, 10);
        textView.setPadding(10, 10, 10, 10);
        hrow.setLayoutParams(hparams);

        hrow.addView(textView);
        return hrow;
    }

    TableRow getRow(String text)
    {
        TableRow hrow = new TableRow(_context);
        hrow.setBackgroundColor(Color.parseColor("#686868"));

        textView = new TextView(_context);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#ffffff"));

        // set params here
        TableRow.LayoutParams hparams = new TableRow.LayoutParams();
        hparams.setMargins(10, 10, 10, 10);
        textView.setPadding(10, 10, 10, 10);
        hrow.setLayoutParams(hparams);

        hrow.addView(textView);
        return hrow;
    }

    TableRow getSeperatorRow()
    {
        TableRow hrow = new TableRow(_context);

        textView = new TextView(_context);
        hrow.setBackgroundColor(Color.parseColor("#ffffff"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setText("");

        // set params here
        TableRow.LayoutParams hparams = new TableRow.LayoutParams();
        hparams.setMargins(10, 10, 10, 10);
        hrow.setLayoutParams(hparams);

        hrow.addView(textView);
        return hrow;
    }

    public void addDonationsReceived(ArrayList<DonationGetter> dons, TableLayout tableLayout)
    {
        for (DonationGetter donor : dons)
        {
            tableLayout.addView(getSeperatorRow());
            tableLayout.addView(getHeaderRow(donor.name));
            tableLayout.addView(getRow(donor.date));
            tableLayout.addView(getRow(donor.description));
        }
    }

    public void addRows(ArrayList<Givings> givingInfos, TableLayout tableLayout, String tableTitle)
    {

        for (Givings ginfo : givingInfos)
        {
            tableLayout.addView(getSeperatorRow());
            tableLayout.addView(getHeaderRow(ginfo.requestSummary));
            tableLayout.addView(getRow(ginfo.itemsDescription));
            tableLayout.addView(getRow(ginfo.donationDate));

        }
    }

}
