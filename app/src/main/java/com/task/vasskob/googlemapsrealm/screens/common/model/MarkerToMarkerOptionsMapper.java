package com.task.vasskob.googlemapsrealm.screens.common.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.utils.Mapper;

public class MarkerToMarkerOptionsMapper implements Mapper<Marker, MarkerOptions> {
    @Override
    public MarkerOptions map(Marker data) {
        LatLng markerLatLng = new LatLng(data.getLatitude(), data.getLongitude());
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(data.getMarkerIcon().getResId());
        return new MarkerOptions().position(markerLatLng).title(data.getTitle()).icon(markerIcon);
    }
}
