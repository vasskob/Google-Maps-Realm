package com.task.vasskob.googlemapsrealm.screens.common.model;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.utils.Mapper;


public class MarkerToClusterItemMapper implements Mapper<Marker, MarkerItem> {
    @Override
    public MarkerItem map(Marker data) {
        return new MarkerItem(data.getLatitude(), data.getLongitude());
    }
}
