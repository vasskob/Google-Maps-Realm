package com.task.vasskob.googlemaps.screens.common.model.mapper;

import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerDbFlow;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.utils.Mapper;


public class MarkerToMarkerDbFlowMapper implements Mapper<Marker, MarkerDbFlow> {
    @Override
    public MarkerDbFlow map(Marker data) {
        MarkerDbFlow marker = new MarkerDbFlow();
        marker.setId(data.getId());
        marker.setTitle(data.getTitle());
        marker.setLatitude(data.getLatitude());
        marker.setLongitude(data.getLongitude());
        marker.setMarkerIcon(data.getMarkerIcon());
        return marker;
    }
}
