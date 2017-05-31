package com.task.vasskob.googlemapsrealm.screens.common.model.mapper;

import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerRealm;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.utils.Mapper;

public class MarkerToMarkerRealmMaper implements Mapper<Marker, MarkerRealm> {
    @Override
    public MarkerRealm map(Marker data) {
        MarkerRealm markerRealm = new MarkerRealm();
        markerRealm.setId(data.getId());
        markerRealm.setTitle(data.getTitle());
        markerRealm.setMarkerIcon(data.getMarkerIcon());
        markerRealm.setLatitude(data.getLatitude());
        markerRealm.setLongitude(data.getLongitude());
        return markerRealm;
    }
}
