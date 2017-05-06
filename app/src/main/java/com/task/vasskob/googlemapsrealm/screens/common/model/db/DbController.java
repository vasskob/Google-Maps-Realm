package com.task.vasskob.googlemapsrealm.screens.common.model.db;

import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import java.util.List;

interface DbController {
    // TODO: 06/05/17 DbController should have any connection with Realm
    void addMarkerToRealm(Marker marker);
    void addMarkerListToRealm(List<Marker> marker);
    void deleteMarkerFromRealm(Marker marker);
    void updateMarkerInRealm(Marker marker, String title, MarkerIcon markerIcon);

}
