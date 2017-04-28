package com.task.vasskob.googlemapsrealm.view;

import com.task.vasskob.googlemapsrealm.model.Marker;

import io.realm.RealmResults;

public interface MapsView {

    void showToast(String msg);

    void showMarkers(final RealmResults<Marker> markers);

}
