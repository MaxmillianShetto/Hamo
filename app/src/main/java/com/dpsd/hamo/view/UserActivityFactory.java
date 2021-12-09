package com.dpsd.hamo.view;

import android.content.Context;

import java.lang.reflect.AnnotatedElement;

public class UserActivityFactory
{
    private Context context;

    public static AnnotatedElement loadActivity(String role)
    {
        if (role.equals("admin"))
        {
            return AdminActivity.class;
        }
        else if (role.equals("rep"))
        {
            return CommunityRepActivity.class;
        }
        else
        {
            return GiverActivity.class;
        }
    }
}
