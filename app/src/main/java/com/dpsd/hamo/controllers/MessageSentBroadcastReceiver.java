package com.dpsd.hamo.controllers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MessageSentBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (Activity.RESULT_OK == getResultCode())
        {
            Toast toast = Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT);
            toast.show();
        }
        context.unregisterReceiver(this);
    }
}