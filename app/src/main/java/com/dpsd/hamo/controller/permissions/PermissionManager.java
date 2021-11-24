package com.dpsd.hamo.controller.permissions;

import android.app.Activity;
import android.content.Context;

public interface PermissionManager
{
    public boolean checkPermission(Context appContext, Activity activity);
}
