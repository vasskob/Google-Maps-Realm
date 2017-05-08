package com.task.vasskob.googlemapsrealm.screens.common.model.db;

import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import java.util.List;

interface DbController {
    void addMarkerToDb(Marker marker);

    void addMarkerListToDb(List<Marker> marker);

    void deleteMarkerInDb(Marker marker);

    void updateMarkerInDb(Marker marker, String title, MarkerIcon markerIcon);

}
