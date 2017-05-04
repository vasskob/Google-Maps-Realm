package com.task.vasskob.googlemapsrealm.screens.marker_details.presenter;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;

interface MarkerInfoPresenter<T> {
    void showMarkerInfoById(String id);
    void updateMarkerInDb(Marker marker, String title, MarkerIcon markerIcon);
    void deleteMarkerInDb(Marker marker);
    void setView(T view);
    void clearView();
}
