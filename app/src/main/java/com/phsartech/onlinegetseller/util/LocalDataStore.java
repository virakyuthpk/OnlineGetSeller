package com.phsartech.onlinegetseller.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LocalDataStore {
    private static String TAG = "localDataStore";

    public static void setLogin(Activity context, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.LOG, value).commit();
        Log.e(TAG, "setLogin: " + value);
    }

    public static boolean getLogin(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constant.LOG, false);
    }

    public static void setID(Activity activity, int value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constant.ID, value).commit();
        Log.e(TAG, "setID: " + value);
    }

    public static int getID(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Constant.ID, 0);
    }

    public static void setSHOPID(Activity activity, int value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constant.SHOPID, value).commit();
        Log.e(TAG, "setSHOPID: " + value);
    }

    public static int getSHOPID(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Constant.SHOPID, 0);
    }

    public static void setToken(Activity activity, String value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.TOKEN, value).commit();
        Log.e(TAG, "setToken: " + value);
    }

    public static String getToken(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STATUS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constant.TOKEN, "");
    }
}
