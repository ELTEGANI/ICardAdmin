package com.madret.icardadmin.Utilites;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by tigani on 11/6/2017.
 */

public class PreferenceManger
{
    private static String TAG = PreferenceManger.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Admin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String PHONE     = "phone";
    private static final String TOTALCARD = "total";
    private static final String USEDCARD  = "usedcard";
    private static final String STATUS    = "status";

    public PreferenceManger(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn)
    {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void SaveCardUsage(String phone,int total,int usedcard,String status)
    {
        editor.putString(PHONE,phone);
        editor.putInt(TOTALCARD,total);
        editor.putInt(USEDCARD,usedcard);
        editor.putString(STATUS,status);
        editor.commit();
    }


    public void SaveUsedCard(int usedcard)
    {
        editor.putInt(USEDCARD,usedcard);
        editor.commit();
    }

    public void SaveStatus(String status)
    {
        editor.putString(STATUS,status);
        editor.commit();
    }



    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public  String GetPhone()
    {
        return pref.getString(PHONE,null);
    }

    public  String GetStatus()
    {
        return pref.getString(STATUS,null);
    }


    public  int GetTotalcard()
    {
        return pref.getInt(TOTALCARD,0);
    }

    public  int GetUsedcard()
    {
        return pref.getInt(USEDCARD,0);
    }

    public void Clear()
    {
        editor.clear();
        editor.commit();
    }

}
