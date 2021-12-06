package com.dpsd.hamo.dbmodel.dbhelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalStorage {
    static SharedPreferences sharedPreferences;

    public static String getValue(String key, Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  sharedPreferences.getString(key,"");
    }

    public static void AddKeyValue(String key,String value,Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
    }
}
