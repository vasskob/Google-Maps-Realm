package com.task.vasskob.googlemapsrealm.screens.map.presenter;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

interface MapsPresenter<T> {
    void setView(T view);

    void clearView();

    void addMarkerToRealm(Marker marker);

    void showMarkersOnMap();

    void removeListener();
}
