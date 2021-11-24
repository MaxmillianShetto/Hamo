package com.dpsd.hamo.controller.permissions;

public class PermissionFactory
{
    public static PermissionManager getPermission(PermissionType permission)
    {
        if (permission == PermissionType.SEND_SMS)
        {
            return new MessagePermission();
        }
        else if (permission == PermissionType.ACCESS_FINE_LOCATION)
        {
            return new LocationPermission();
        }
        else if (permission == PermissionType.MEDIA)
        {
            return new MediaPermission();
        }
        else if (permission == PermissionType.CAMERA)
        {
            return new CameraPermission();
        }
        else if (permission == PermissionType.INTERNET)
        {
            return new InternetPermission();
        }
        return null;
    }
}
