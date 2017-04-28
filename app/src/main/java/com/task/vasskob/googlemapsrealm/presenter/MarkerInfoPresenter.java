package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

interface MarkerInfoPresenter<T> {
    void showMarkerInfoById(int id);
    void updateMarkerInDb(Marker marker, String title, MarkerIcon markerIcon);
    void deleteMarkerInDb(Marker marker);
    void setView(T view);
    void clearView();
}
