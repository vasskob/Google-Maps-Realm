package com.task.vasskob.googlemapsrealm.screens.common.model;

import io.realm.RealmObject;

public class MarkerIcon extends RealmObject {

    private int id;
    private int resId;
    private boolean isSelected;

    public MarkerIcon(int id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MarkerIcon() {
    }

    public int getResId() {
        return resId;
    }

    public int getId() {
        return id;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setId(int id) {
        this.id = id;
    }

}
