package com.example.notebook.onlineshop.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gamatechno Developer on 15/07/2016.
 */
public class PreferencesManager {
    public static final String loginStatus  = "login_status";
    public static final String userId       = "user_id";
    public static final String username     = "user_name";
    public static final String packageName  = "onlineshop";

    public static void setPreferenceValue(Context context, String pname, String str){
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(packageName+pname, str);
        editor.apply();
    }

    public static String getPreferanceValue(Context context, String pname){
        String data = "";
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        data = sharedPref.getString(packageName+pname, "");
        return data;
    }
}
