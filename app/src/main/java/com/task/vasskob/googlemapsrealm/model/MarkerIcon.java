package com.task.vasskob.googlemapsrealm.model;


// TODO: 25/04/17 use this initialization to store relation between icon id and title
public class MarkerIcon {
    private final int mResID;
    private final int mTitle;

    public MarkerIcon(int resID, int title) {
        mResID = resID;
        mTitle = title;
    }

    public int getResID() {
        return mResID;
    }

    public int getTitle() {
        return mTitle;
    }
}
