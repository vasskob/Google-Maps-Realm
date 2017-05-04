package com.task.vasskob.googlemapsrealm.screens.map.model;

import com.google.android.gms.maps.model.LatLng;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.google.maps.android.clustering.ClusterItem;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Marker extends RealmObject implements ClusterItem {

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

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
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
