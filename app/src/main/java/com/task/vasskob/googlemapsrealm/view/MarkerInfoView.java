package com.task.vasskob.googlemapsrealm.view;

import com.task.vasskob.googlemapsrealm.model.Marker;

public interface MarkerInfoView {

    void showMarkerInfo(Marker marker);

    // TODO: 03/05/17 use meaningful names. Methods don't used, do you need them?
    // TODO: 03/05/17 why Toast?
    void showErrorToast();

    void showDeleteToast();

    void showUpdateToast();
}
