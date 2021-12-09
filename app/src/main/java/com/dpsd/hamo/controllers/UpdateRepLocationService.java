package com.dpsd.hamo.controllers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.dpsd.hamo.R;
import com.dpsd.hamo.view.ui.home.DonationDetailsActivity;

public class UpdateRepLocationService extends Service
{
    private final int COMMUNITY_REP_LOCATION_REQUEST_CODE = 10;
    private final int COMMUNITY_REP_LOCATION = 1;
    private final long COMMUNITY_REP_LOCATION_INTERVAL = 5000;
    private final String CHANNEL_ID = "Location";
    private final boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    private LocationRequest communityRepLocation;
    private FusedLocationProviderClient fusedRepLocationClient;



    public UpdateRepLocationService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        notifyUser();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notifyUser()
    {
        if(runningQOrLater)
        {
            NotificationChannel locationServiceChannel = new NotificationChannel(
                    CHANNEL_ID, "Community Rep Location",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(locationServiceChannel);
        }
            Intent communityRepLocationIntent = new Intent(this, DonationDetailsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, COMMUNITY_REP_LOCATION_REQUEST_CODE,
                    communityRepLocationIntent, 0);
            Notification locationNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(COMMUNITY_REP_LOCATION,locationNotification);
    }

    void communityRepLocationMonitor()
    {
        communityRepLocation = LocationRequest.create();
        communityRepLocation.setInterval(COMMUNITY_REP_LOCATION_INTERVAL);
        communityRepLocation.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}