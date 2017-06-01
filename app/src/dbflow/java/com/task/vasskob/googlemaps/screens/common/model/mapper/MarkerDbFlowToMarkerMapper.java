package com.task.vasskob.googlemaps.screens.common.model.mapper;

import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerDbFlow;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.utils.Mapper;


public class MarkerDbFlowToMarkerMapper implements Mapper<MarkerDbFlow, Marker> {
    @Override
    public Marker map(MarkerDbFlow data) {
        Marker marker = new Marker();
        marker.setId(data.getId());
        marker.setTitle(data.getTitle());
        marker.setLatitude(data.getLatitude());
        marker.setLongitude(data.getLongitude());
        marker.setMarkerIcon(data.getMarkerIcon());
        return marker;
    }
}
