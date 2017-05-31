package com.task.vasskob.googlemapsrealm.screens.marker_details.presenter;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

interface MarkerInfoPresenter<T> {
    void showMarkerInfoById(String id);

    void updateMarkerInDb(Marker marker);
    void deleteMarkerInDb(Marker marker);
    void setView(T view);
    void clearView();
}
