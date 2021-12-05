package com.dpsd.hamo.view;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dpsd.hamo.R;

import java.lang.reflect.AnnotatedElement;

public class UserActivityFactory
{
    private  Context context;

    public static AnnotatedElement loadActivity(String role)
    {
        if(role.equals("admin"))
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
