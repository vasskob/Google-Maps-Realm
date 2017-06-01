package com.task.vasskob.googlemaps.screens.common.model.mapper;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.utils.Mapper;

public class MarkerToMarkerOptionsMapper implements Mapper<Marker, MarkerOptions> {
    private static final String TAG = MarkerToMarkerOptionsMapper.class.getSimpleName();
    @Override
    public MarkerOptions map(Marker data) {
        LatLng markerLatLng = new LatLng(data.getLatitude(), data.getLongitude());
        // TODO: 01.06.17
        Log.d(TAG, "map: marker id = " + data.getTitle());
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(data.getMarkerIcon().getResId());
        return new MarkerOptions().position(markerLatLng).title(data.getTitle()).icon(markerIcon);
    }
}
