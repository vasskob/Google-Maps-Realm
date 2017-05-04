package com.task.vasskob.googlemapsrealm.screens.common.model.db;

import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

interface DbController {

    void addMarkerToRealm(Marker marker);
    void deleteMarkerFromRealm(Marker marker);
    void updateMarkerInRealm(Marker marker, String title, MarkerIcon markerIcon);

}
