package com.task.vasskob.googlemapsrealm.view;

import com.task.vasskob.googlemapsrealm.model.Marker;

public interface MapView {

    void showMarkerOnMap(Marker marker);
    void showToast(String msg);

}
