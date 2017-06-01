package com.task.vasskob.googlemaps.screens.detail.view;

import com.task.vasskob.googlemaps.screens.map.model.Marker;

public interface MarkerInfoView {

    void showMarkerInfo(Marker marker);

    void closeActivity();

    void showError();
}
