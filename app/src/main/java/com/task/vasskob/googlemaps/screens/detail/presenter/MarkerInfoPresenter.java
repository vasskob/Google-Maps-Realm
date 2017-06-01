package com.task.vasskob.googlemaps.screens.detail.presenter;

import com.task.vasskob.googlemaps.screens.map.model.Marker;

interface MarkerInfoPresenter<T> {
    void showMarkerInfoById(String id);

    void updateMarkerInDb(Marker marker);
    void deleteMarkerInDb(Marker marker);
    void setView(T view);
    void clearView();
}
