package com.task.vasskob.googlemaps.screens.common.model.mapper;

import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerClusterItem;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.utils.Mapper;


public class MarkerToClusterItemMapper implements Mapper<Marker, MarkerClusterItem> {
    @Override
    public MarkerClusterItem map(Marker data) {
        return new MarkerClusterItem(data.getLatitude(), data.getLongitude());
    }
}
