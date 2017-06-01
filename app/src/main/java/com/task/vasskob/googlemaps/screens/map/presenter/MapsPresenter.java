package com.task.vasskob.googlemaps.screens.map.presenter;

import com.task.vasskob.googlemaps.screens.map.model.Marker;

interface MapsPresenter<T> {
    void setView(T view);

    void clearView();

    void addMarkerToRealm(Marker marker);

    void showMarkersOnMap();

    void removeListener();
}
