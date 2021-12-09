package com.dpsd.hamo.controller.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class MessagePermission implements PermissionManager
{
    private static final int PERMISSIONS_REQUEST_CODE = 5;
    private final boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    public boolean checkPermission(Context appContext, Activity activity)
    {
        if (!runningQOrLater)
        {
            return true;
        }
        else
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_CODE);

            return (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    appContext, Manifest.permission.SEND_SMS));
        }
    }
}
