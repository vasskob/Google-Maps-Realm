package com.task.vasskob.googlemapsrealm.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";
    private static final String ICON_LOADED = "iconLoaded";
    private static Prefs instance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context) {

        if (instance == null) {
            instance = new Prefs(context);
        }
        return instance;
    }

    public void setPreLoad(boolean isLoaded) {

        sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, isLoaded)
                .apply();
    }

    public void setMarkerIconsLoad(boolean isLoaded) {

        sharedPreferences
                .edit()
                .putBoolean(ICON_LOADED, isLoaded)
                .apply();
    }

    public boolean getPreLoad(){
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }

    public boolean getMarkerIconsLoad(){
        return sharedPreferences.getBoolean(ICON_LOADED, false);
    }

}