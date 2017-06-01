package com.task.vasskob.googlemaps.screens.common.model.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.task.vasskob.googlemaps.screens.common.model.db.DbFlowDatabase;

@Table(database = DbFlowDatabase.class)
public class MarkerDbFlow extends BaseModel {

    @Column
    @PrimaryKey
    private String id;
    @Column
    private String title;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    @ForeignKey
    private MarkerIcon markerIcon;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public MarkerIcon getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(MarkerIcon markerIcon) {
        this.markerIcon = markerIcon;
    }
}
