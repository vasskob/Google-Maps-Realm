package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;

interface MapsPresenter<T> {
    void setView(T view);

    void clearView();

    void addMarkerToRealm(Marker marker);

    void showMarkersOnMap();

    void updateRealm();

    void closeRealm();
}
