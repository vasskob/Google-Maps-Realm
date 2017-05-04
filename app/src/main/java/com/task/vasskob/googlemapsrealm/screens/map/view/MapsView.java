package com.task.vasskob.googlemapsrealm.screens.map.view;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import io.realm.RealmResults;

public interface MapsView {

  void showMarkersOnMap(final RealmResults<Marker> markers);
}
