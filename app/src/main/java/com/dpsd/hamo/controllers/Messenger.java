package com.dpsd.hamo.controllers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.dpsd.hamo.controller.permissions.PermissionFactory;
import com.dpsd.hamo.controller.permissions.PermissionManager;
import com.dpsd.hamo.controller.permissions.PermissionType;

public class Messenger
{
    private SmsManager smsManager;
    private BroadcastReceiver messageSentReceiver = new MessageSentBroadcastReceiver();

    public Messenger()
    {
        smsManager = SmsManager.getDefault();
    }

    public void sendMessage(Context appContext, Activity activity, String phoneNumber, String message)
    {
        PermissionManager permissionsManager = PermissionFactory.getPermission(PermissionType.SEND_SMS);
        assert permissionsManager != null;
        if (permissionsManager.checkPermission(appContext, activity))
        {
            appContext.registerReceiver(messageSentReceiver, new IntentFilter("SMS_SENT_ACTION"));
            PendingIntent messageSent = PendingIntent.getBroadcast(appContext,0, new Intent("SMS_SENT_ACTION"), 0);
            smsManager.sendTextMessage(phoneNumber,null, message, messageSent, null);
        }
    }
}
