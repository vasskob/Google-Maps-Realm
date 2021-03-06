package com.task.vasskob.googlemaps.screens.common.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MarkerRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String title;
    private MarkerIcon markerIcon;
    private Double latitude;
    private Double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MarkerIcon getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(MarkerIcon markerIcon) {
        this.markerIcon = markerIcon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
