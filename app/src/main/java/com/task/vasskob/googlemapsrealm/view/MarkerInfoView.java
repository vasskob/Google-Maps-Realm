package com.task.vasskob.googlemapsrealm.view;

import com.task.vasskob.googlemapsrealm.model.Marker;

public interface MarkerInfoView {

    void showMarkerInfo(Marker marker);

    void showErrorToast();

    void showDeleteToast();

    void showUpdateToast();
}
